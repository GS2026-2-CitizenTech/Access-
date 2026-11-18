document.addEventListener("DOMContentLoaded", () => {
    const formatter = new Intl.NumberFormat("pt-BR", {
        minimumFractionDigits: 0,
        maximumFractionDigits: 1
    });

    document.querySelectorAll("[data-rating-widget]").forEach((widget) => {
        const inputs = [...widget.querySelectorAll(".rating-choice")];
        const output = widget.querySelector("[data-rating-output]");
        const clearButton = widget.querySelector("[data-rating-clear]");

        const update = () => {
            const selected = inputs.find((input) => input.checked);
            const value = selected && selected.value !== "" ? Number(selected.value) : null;

            widget.dataset.value = value ?? "";
            widget.dataset.empty = value === null ? "true" : "false";

            if (value === null) {
                output.textContent = widget.querySelector(".rating-choice--not-informed")
                    ? "Não informar"
                    : "Selecione uma nota";
                return;
            }

            output.textContent = `${formatter.format(value)} / 5`;
        };

        inputs.forEach((input) => input.addEventListener("change", update));
        clearButton?.addEventListener("click", () => {
            inputs.forEach((input) => { input.checked = false; });
            update();
            inputs[0]?.focus();
        });

        update();
    });
});
