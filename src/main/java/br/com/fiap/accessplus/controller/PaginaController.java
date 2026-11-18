package br.com.fiap.accessplus.controller;

import br.com.fiap.accessplus.bo.AvaliacaoBO;
import br.com.fiap.accessplus.bo.FormularioAvaliacaoBO;
import br.com.fiap.accessplus.bo.RecursoAcessibilidadeBO;
import br.com.fiap.accessplus.bo.RestauranteBO;
import br.com.fiap.accessplus.bo.RestauranteRecursoBO;
import br.com.fiap.accessplus.bo.UsuarioBO;
import br.com.fiap.accessplus.bo.VerificacaoRecursoBO;
import br.com.fiap.accessplus.exception.DeclaracaoRecursoNaoEncontradaException;
import br.com.fiap.accessplus.exception.UsuarioNaoEncontradoException;
import br.com.fiap.accessplus.exception.VerificacaoRecursoDuplicadaException;
import br.com.fiap.accessplus.model.dto.FormularioAvaliacaoDTO;
import br.com.fiap.accessplus.model.entity.Avaliacao;
import br.com.fiap.accessplus.model.entity.RecursoAcessibilidade;
import br.com.fiap.accessplus.model.entity.Restaurante;
import br.com.fiap.accessplus.model.entity.RestauranteRecurso;
import br.com.fiap.accessplus.model.entity.StatusDeclaracao;
import br.com.fiap.accessplus.model.entity.TipoUsuario;
import br.com.fiap.accessplus.model.entity.VerificacaoRecurso;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/app")
public class PaginaController {

    private final RestauranteBO restauranteBO;
    private final RecursoAcessibilidadeBO recursoBO;
    private final RestauranteRecursoBO restauranteRecursoBO;
    private final AvaliacaoBO avaliacaoBO;
    private final VerificacaoRecursoBO verificacaoRecursoBO;
    private final UsuarioBO usuarioBO;
    private final FormularioAvaliacaoBO formularioAvaliacaoBO;

    public PaginaController(RestauranteBO restauranteBO,
                            RecursoAcessibilidadeBO recursoBO,
                            RestauranteRecursoBO restauranteRecursoBO,
                            AvaliacaoBO avaliacaoBO,
                            VerificacaoRecursoBO verificacaoRecursoBO,
                            UsuarioBO usuarioBO,
                            FormularioAvaliacaoBO formularioAvaliacaoBO) {
        this.restauranteBO = restauranteBO;
        this.recursoBO = recursoBO;
        this.restauranteRecursoBO = restauranteRecursoBO;
        this.avaliacaoBO = avaliacaoBO;
        this.verificacaoRecursoBO = verificacaoRecursoBO;
        this.usuarioBO = usuarioBO;
        this.formularioAvaliacaoBO = formularioAvaliacaoBO;
    }

    @GetMapping({"", "/"})
    public String inicio(Model model) {
        List<Restaurante> restaurantes = restauranteBO.listarPublicos().stream()
                .sorted(Comparator.comparing(Restaurante::getNome, String.CASE_INSENSITIVE_ORDER))
                .limit(5)
                .toList();

        model.addAttribute("restaurantes", restaurantes);
        model.addAttribute("recursosPorRestaurante", recursosPorRestaurante(restaurantes));
        model.addAttribute("recursos", recursoBO.listarTodos());
        model.addAttribute("activeNav", "inicio");
        return "inicio";
    }

    @GetMapping("/explorar")
    public String explorar(@RequestParam(required = false) String busca,
                           @RequestParam(required = false) Long recurso,
                           Model model) {
        List<Restaurante> restaurantes = recurso == null
                ? restauranteBO.listarPublicos()
                : restauranteRecursoBO.listarRestaurantesPublicosComRecurso(recurso);

        List<Restaurante> filtrados = restaurantes.stream()
                .filter(restaurante -> correspondeBusca(restaurante, busca))
                .sorted(Comparator.comparing(Restaurante::getNome, String.CASE_INSENSITIVE_ORDER))
                .toList();

        model.addAttribute("restaurantes", filtrados);
        model.addAttribute("recursosPorRestaurante", recursosPorRestaurante(filtrados));
        model.addAttribute("recursos", recursoBO.listarTodos());
        model.addAttribute("busca", busca == null ? "" : busca);
        model.addAttribute("recursoSelecionado", recurso);
        model.addAttribute("activeNav", "explorar");
        return "explorar";
    }

    @GetMapping("/restaurantes/{idRestaurante}")
    public String detalhes(@PathVariable Long idRestaurante, Model model) {
        Restaurante restaurante = restauranteBO.buscarPublicoPorId(idRestaurante);
        List<RestauranteRecurso> declaracoes = restauranteRecursoBO
                .listarDeclaracoesPublicasDoRestaurante(idRestaurante);
        List<Avaliacao> avaliacoes = avaliacaoBO.listarPublicasDoRestaurante(idRestaurante);

        Map<Long, List<VerificacaoRecurso>> verificacoesPorAvaliacao = avaliacoes.stream()
                .collect(Collectors.toMap(Avaliacao::getIdAvaliacao,
                        avaliacao -> verificacaoRecursoBO.listarPublicasDaAvaliacao(avaliacao.getIdAvaliacao()),
                        (primeira, segunda) -> primeira,
                        LinkedHashMap::new));

        model.addAttribute("restaurante", restaurante);
        model.addAttribute("declaracoes", declaracoes);
        model.addAttribute("avaliacoes", avaliacoes);
        model.addAttribute("verificacoesPorAvaliacao", verificacoesPorAvaliacao);
        model.addAttribute("notaMedia", calcularMedia(avaliacoes));
        model.addAttribute("activeNav", "explorar");
        return "detalhe-restaurante";
    }

    @GetMapping("/restaurantes/{idRestaurante}/avaliar")
    public String avaliar(@PathVariable Long idRestaurante,
                          @ModelAttribute("formularioAvaliacao") FormularioAvaliacaoDTO formulario,
                          Model model) {
        prepararFormulario(idRestaurante, formulario, model);
        return "avaliar-restaurante";
    }

    @org.springframework.web.bind.annotation.PostMapping("/restaurantes/{idRestaurante}/avaliar")
    public String salvarAvaliacao(@PathVariable Long idRestaurante,
                                  @Valid @ModelAttribute("formularioAvaliacao") FormularioAvaliacaoDTO formulario,
                                  BindingResult bindingResult,
                                  Model model,
                                  RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            adicionarMensagensDeErro(bindingResult, model);
            prepararFormulario(idRestaurante, formulario, model);
            return "avaliar-restaurante";
        }

        try {
            formularioAvaliacaoBO.cadastrar(idRestaurante, formulario);
        } catch (IllegalArgumentException | UsuarioNaoEncontradoException
                 | DeclaracaoRecursoNaoEncontradaException | VerificacaoRecursoDuplicadaException ex) {
            bindingResult.reject("formulario", ex.getMessage());
            adicionarMensagensDeErro(bindingResult, model);
            prepararFormulario(idRestaurante, formulario, model);
            return "avaliar-restaurante";
        }

        redirectAttributes.addFlashAttribute("sucesso", "Avaliação enviada com sucesso. Obrigado pelo seu relato!");
        return "redirect:/app/restaurantes/" + idRestaurante;
    }

    @GetMapping("/avaliacoes")
    public String avaliacoes(Model model) {
        model.addAttribute("activeNav", "avaliacoes");
        return "avaliacoes";
    }

    @GetMapping("/sobre")
    public String sobre(Model model) {
        model.addAttribute("activeNav", "sobre");
        return "sobre";
    }

    private Map<Long, List<RestauranteRecurso>> recursosPorRestaurante(List<Restaurante> restaurantes) {
        return restaurantes.stream().collect(Collectors.toMap(Restaurante::getIdRestaurante,
                restaurante -> restauranteRecursoBO.listarDeclaracoesPublicasDoRestaurante(restaurante.getIdRestaurante())
                        .stream()
                        .filter(declaracao -> declaracao.getStatusDeclaracao() == StatusDeclaracao.SIM)
                        .limit(3)
                        .toList(),
                (primeira, segunda) -> primeira,
                LinkedHashMap::new));
    }

    private void prepararFormulario(Long idRestaurante, FormularioAvaliacaoDTO formulario, Model model) {
        model.addAttribute("formularioAvaliacao", formulario);
        Restaurante restaurante = restauranteBO.buscarPublicoPorId(idRestaurante);
        List<RestauranteRecurso> declaracoes = restauranteRecursoBO.listarDeclaracoesPublicasDoRestaurante(idRestaurante);
        while (formulario.getVerificacoes().size() < declaracoes.size()) {
            formulario.getVerificacoes().add(new FormularioAvaliacaoDTO.VerificacaoFormularioDTO());
        }
        for (int indice = 0; indice < declaracoes.size(); indice++) {
            FormularioAvaliacaoDTO.VerificacaoFormularioDTO verificacao = formulario.getVerificacoes().get(indice);
            if (verificacao.getIdRestauranteRecurso() == null) {
                verificacao.setIdRestauranteRecurso(declaracoes.get(indice).getIdRestauranteRecurso());
            }
        }
        model.addAttribute("restaurante", restaurante);
        model.addAttribute("declaracoes", declaracoes);
        model.addAttribute("usuariosDemonstracao", usuarioBO.listarTodos().stream()
                .filter(usuario -> usuario.getTipoUsuario() == TipoUsuario.CONSUMIDOR)
                .toList());
        model.addAttribute("activeNav", "avaliacoes");
    }

    private void adicionarMensagensDeErro(BindingResult bindingResult, Model model) {
        model.addAttribute("errosFormulario", bindingResult.getAllErrors().stream()
                .map(erro -> erro.getDefaultMessage())
                .filter(Objects::nonNull)
                .distinct()
                .toList());
    }

    private boolean correspondeBusca(Restaurante restaurante, String busca) {
        if (busca == null || busca.isBlank()) {
            return true;
        }
        String termo = busca.trim().toLowerCase(Locale.ROOT);
        return contem(restaurante.getNome(), termo)
                || contem(restaurante.getBairro(), termo)
                || contem(restaurante.getCidade(), termo);
    }

    private boolean contem(String valor, String termo) {
        return valor != null && valor.toLowerCase(Locale.ROOT).contains(termo);
    }

    private BigDecimal calcularMedia(List<Avaliacao> avaliacoes) {
        if (avaliacoes.isEmpty()) {
            return null;
        }
        BigDecimal soma = avaliacoes.stream()
                .map(Avaliacao::getNotaGeral)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return soma.divide(BigDecimal.valueOf(avaliacoes.size()), 1, RoundingMode.HALF_UP);
    }
}
