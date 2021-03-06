package wypozyczalnia.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class Encryption {
    @Autowired
    private PasswordEncoder encoder;

    /**
     * Method is encoding password
     *
     * @param password
     * @return
     */
    public String encode(String password) {
        return encoder.encode(password);
    }
}
