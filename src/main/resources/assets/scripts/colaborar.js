function logout() {
    console.log("Cerrando sesión...");
    localStorage.removeItem('userName');
    document.cookie = "session=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;";
    window.location.href = "/home";
}