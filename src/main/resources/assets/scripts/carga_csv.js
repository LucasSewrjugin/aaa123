const dropArea = document.getElementById('drop-area');
const fileInput = document.getElementById('csv-file');
const fileNameDisplay = document.getElementById('file-name');

// Evita el comportamiento predeterminado
['dragenter', 'dragover', 'dragleave', 'drop'].forEach(eventName => {
    dropArea.addEventListener(eventName, (e) => e.preventDefault(), false);
    dropArea.addEventListener(eventName, (e) => e.stopPropagation(), false);
});

// Cambia el estilo al arrastrar un archivo
['dragenter', 'dragover'].forEach(eventName => {
    dropArea.addEventListener(eventName, () => dropArea.classList.add('dragover'), false);
});

// Quita el estilo cuando el archivo no está siendo arrastrado sobre el área
['dragleave', 'drop'].forEach(eventName => {
    dropArea.addEventListener(eventName, () => dropArea.classList.remove('dragover'), false);
});

// Muestra el archivo cuando se suelta
dropArea.addEventListener('drop', (e) => {
    const file = e.dataTransfer.files[0];
    handleFile(file);
});

// Abre el selector de archivos cuando se hace clic en el área
dropArea.addEventListener('click', () => fileInput.click());

// Muestra el archivo cuando se selecciona a través del botón
fileInput.addEventListener('change', (e) => {
    const file = e.target.files[0];
    handleFile(file);
});

// Función para manejar y mostrar el nombre del archivo
function handleFile(file) {
    fileNameDisplay.textContent = `Archivo seleccionado: ${file.name}`;
}