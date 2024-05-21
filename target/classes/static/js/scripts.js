document.getElementById('registerForm').addEventListener('submit', async (e) => {
    e.preventDefault();

    const usuario = {
        nombreUsuario: document.getElementById('registerNombreUsuario').value,
        contrasena: document.getElementById('registerContrasena').value,
        nombre: document.getElementById('registerNombre').value,
        carrera: document.getElementById('registerCarrera').value,
        edad: parseInt(document.getElementById('registerEdad').value),
        genero: document.getElementById('registerGenero').value,
        afluenciaPreferida: document.getElementById('registerAfluenciaPreferida').value,
        intereses: document.getElementById('registerIntereses').value.split(','),
        clubesAsistidos: document.getElementById('registerClubesAsistidos').value.split(','),
        accionesPreferidas: document.getElementById('registerAccionesPreferidas').value.split(',')
    };

    const response = await fetch('/api/auth/register', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(usuario)
    });

    const message = await response.text();
    alert(message);
});

document.getElementById('loginForm').addEventListener('submit', async (e) => {
    e.preventDefault();

    const usuario = {
        nombreUsuario: document.getElementById('loginNombreUsuario').value,
        contrasena: document.getElementById('loginContrasena').value
    };

    const response = await fetch('/api/auth/login', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(usuario)
    });

    const message = await response.text();
    alert(message);
});
