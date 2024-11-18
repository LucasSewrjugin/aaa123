document.addEventListener("DOMContentLoaded", () => {
    const userDropdown = document.getElementById("user-dropdown");
    const optionsDropdown = document.getElementById("options-dropdown")

    if (userDropdown) {
        userDropdown.addEventListener("click", (e) => {
            e.stopPropagation(); // Evita que el evento se propague
            userDropdown.classList.toggle("active"); // Alterna la clase activa
        });

        // Cierra el menú al hacer clic fuera
        document.addEventListener("click", () => {
            userDropdown.classList.remove("active");
        });
    }

    if (optionsDropdown) {
        optionsDropdown.addEventListener("click", (e) => {
            e.stopPropagation(); // Evita que el evento se propague
            optionsDropdown.classList.toggle("active"); // Alterna la clase activa
        });

        // Cierra el menú al hacer clic fuera
        document.addEventListener("click", () => {
            optionsDropdown.classList.remove("active");
        });
    }
});