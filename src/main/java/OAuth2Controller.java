
import com.myweb.pojo.User;
import com.myweb.services.UserService;
import com.myweb.utils.GeneralUtils;
import com.myweb.utils.JwtUtils;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author huaquangdat
 */
@RestController
@RequestMapping("/api/oauth2")
public class OAuth2Controller {

    private static final Logger logger = LoggerFactory.getLogger(OAuth2Controller.class);

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtils jwtUtils;

    @GetMapping("/success")
    public ResponseEntity<?> handleOAuth2Success(Authentication authentication) throws Exception {
        if (authentication == null || !(authentication.getPrincipal() instanceof OAuth2User)) {
            logger.error("Invalid OAuth2 authentication");
            return ResponseEntity.status(401).body("Invalid OAuth2 authentication");
        }

        OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
        String email = oauth2User.getAttribute("email");
        String name = oauth2User.getAttribute("name");
        String picture = oauth2User.getAttribute("picture");

        logger.debug("OAuth2 User: email={}, name={}, picture={}", email, name, picture);

        // Kiểm tra hoặc tạo người dùng
        User user = userService.getUserByUsername(email);
        if (user == null) {
            // Tạo người dùng mới với vai trò mặc định là ROLE_CANDIDATE
            Map<String, String> params = new HashMap<>();
            params.put("username", email);
            params.put("fullname", name);
            params.put("email", email);
            params.put("role", GeneralUtils.Role.ROLE_CANDIDATE.name());
            user = userService.addUser(params, null); // Không cần avatar từ Google
            logger.info("Created new user: {}", email);
        }

        // Tạo JWT token
        String token = jwtUtils.generateToken(user.getUsername(), List.of(user.getRole()));
        Map<String, String> response = new HashMap<>();
        response.put("token", token);
        response.put("username", user.getUsername());
        response.put("role", user.getRole());

        return ResponseEntity.ok(response);
    }
}
