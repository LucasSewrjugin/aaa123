let currentSlide = 0;

function showSlide(index) {
    const slides = document.querySelectorAll('.carousel-images img');
    const totalSlides = slides.length;

    if (index >= totalSlides) currentSlide = 0;
    else if (index < 0) currentSlide = totalSlides - 1;
    else currentSlide = index;

    const offset = -currentSlide * 100;
    document.querySelector('.carousel-images').style.transform = `translateX(${offset}%)`;
}

function moveSlide(step) {
    showSlide(currentSlide + step);
}

// Cambio automático cada 5 segundos
setInterval(() => moveSlide(1), 5000);

// Verifica si el usuario está logueado
window.onload = function() {
    const userName = localStorage.getItem('userName'); // Obtiene el nombre del usuario desde el almacenamiento local

    if (userName) {
        // Si el usuario está logueado, muestra su nombre
        document.getElementById('user-name').style.display = 'inline-block';
        document.getElementById('user-fullname').textContent = userName;
        document.getElementById('login-link').style.display = 'none'; // Oculta el enlace de login
    } else {
        // Si no está logueado, muestra el botón de login
        document.getElementById('login-link').style.display = 'inline-block';
        document.getElementById('user-name').style.display = 'none'; // Oculta el nombre de usuario
    }
}