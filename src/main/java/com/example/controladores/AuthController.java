package com.example.controladores;

@Controller
public class AuthController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/login")
    public String mostrarLogin() {
        return "login";
    }

    @GetMapping("/registro")
    public String mostrarRegistro(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "registro";
    }

    @PostMapping("/registro")
    public String registrarUsuario(@ModelAttribute Usuario usuario, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "registro";
        }
        usuarioService.registrarUsuario(usuario);
        return "redirect:/registro-estudiante";
    }

    @GetMapping("/registro-estudiante")
    public String mostrarRegistroEstudiante(Model model) {
        model.addAttribute("estudiante", new Estudiante());
        return "registro-estudiante";
    }

    @PostMapping("/registro-estudiante")
    public String registrarEstudiante(@ModelAttribute Estudiante estudiante, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "registro-estudiante";
        }
        // Aquí puedes guardar el estudiante en la base de datos y asociarlo al usuario
        // Lógica de negocio para asociar estudiante al usuario
        return "redirect:/home";
    }
}
