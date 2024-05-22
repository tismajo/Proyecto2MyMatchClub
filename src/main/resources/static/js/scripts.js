// Importar el controlador Neo4j para JavaScript
const neo4j = require('neo4j-driver');

window.addEventListener('DOMContentLoaded', event => {
    // Crear una instancia del controlador Neo4j
    const driver = neo4j.driver("neo4j+s://87fbfc54.databases.neo4j.io", neo4j.auth.basic("neo4j", "d95kqzeZGl5YcfOxDxuNQ7PZ_TYH2dUaiXxq2n6yNKc"));

    // Navbar shrink function
    var navbarShrink = function () {
        const navbarCollapsible = document.body.querySelector('#mainNav');
        if (!navbarCollapsible) {
            return;
        }
        if (window.scrollY === 0) {
            navbarCollapsible.classList.remove('navbar-shrink')
        } else {
            navbarCollapsible.classList.add('navbar-shrink')
        }
    };

    // Shrink the navbar
    navbarShrink();

    // Shrink the navbar when page is scrolled
    document.addEventListener('scroll', navbarShrink);

    // Activate Bootstrap scrollspy on the main nav element
    const mainNav = document.body.querySelector('#mainNav');
    if (mainNav) {
        new bootstrap.ScrollSpy(document.body, {
            target: '#mainNav',
            rootMargin: '0px 0px -40%',
        });
    };

    // Collapse responsive navbar when toggler is visible
    const navbarToggler = document.body.querySelector('.navbar-toggler');
    const responsiveNavItems = [].slice.call(
        document.querySelectorAll('#navbarResponsive .nav-link')
    );
    responsiveNavItems.map(function (responsiveNavItem) {
        responsiveNavItem.addEventListener('click', () => {
            if (window.getComputedStyle(navbarToggler).display !== 'none') {
                navbarToggler.click();
            }
        });
    });

    // Handle registration form submission
    document.getElementById('registerForm').addEventListener('submit', async (e) => {
        e.preventDefault();

        const usuario = {
            nombreUsuario: document.getElementById('nombreUsuario').value,
            contrasena: document.getElementById('contrasena').value,
            nombre: document.getElementById('nombre').value,
            carrera: document.getElementById('carrera').value,
            edad: parseInt(document.getElementById('edad').value),
            genero: document.getElementById('genero').value,
            afluenciaPreferida: document.getElementById('afluenciaPreferida').value,
            intereses: document.getElementById('intereses').value.split(',').map(i => i.trim()),
            clubesAsistidos: document.getElementById('clubesAsistidos').value.split(',').map(c => c.trim()),
            accionesPreferidas: document.getElementById('accionesPreferidas').value.split(',').map(a => a.trim())
        };

        try {
            const response = await fetch('/api/auth/register', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(usuario)
            });

            if (response.ok) {
                console.log('Usuario registrado con éxito.');
                document.getElementById('registroExitoso').style.display = 'block';
            } else {
                console.error('Error al registrar el usuario');
                alert('Error al registrar el usuario');
            }
        } catch (error) {
            console.error('Error al enviar la solicitud:', error);
            alert('Error al registrar el usuario');
        }
    });

    // Handle login form submission (ejemplo)
    document.getElementById('loginForm').addEventListener('submit', async (e) => {
        e.preventDefault();

        const usuario = {
            nombreUsuario: document.getElementById('loginNombreUsuario').value,
            contrasena: document.getElementById('loginContrasena').value
        };

        try {
            const response = await fetch('/api/auth/login', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(usuario)
            });

            if (!response.ok) {
                throw new Error('Error en la solicitud');
            }

            const result = await response.json();
            console.log(result);
            // Muestra el mensaje de inicio de sesión exitoso
            document.getElementById('inicioSesionExitoso').style.display = 'block';
        } catch (error) {
            console.error('Error al iniciar sesión:', error);
            alert('Error al iniciar sesión');
        }
    });
});
