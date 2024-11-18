var map = L.map('map').setView([-34.627467, -58.448736], 14);

       L.tileLayer('https://tile.openstreetmap.org/{z}/{x}/{y}.png', {
           maxZoom: 25,
           minZoom: 2,
           attribution: '&copy; <a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a>'
       }).addTo(map);

        //Clickeo el mapa y me da un mensaje con las coordenadas del punto clickeado
        var popup = L.popup();

          function onMapClick(e) {
              popup
                  .setLatLng(e.latlng)
                  .setContent("You clicked the map at " + e.latlng.toString())
                  .openOn(map);
          }

          map.on('click', onMapClick);


        // Agregar icono custom
        var heladeraIcon = L.icon({
            iconUrl: '/assets/images/heladera.png',
            iconSize: [50, 50]
        });

heladerasLocation.forEach(heladera => {
    L.marker([heladera.latitud, heladera.longitud], {icon: heladeraIcon, title: heladera.nombre}).addTo(map).bindPopup(`<div class="popup-container"><p class="popup-text">${heladera.nombre}</p><button class="popup-button" onclick="window.location.href='/heladeras/${heladera.id}'">Más Información</button></div>`);
})