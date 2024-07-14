package kaz.olzhas.ylab.state;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Authentication {
    private String username;
    private boolean isAuth;

    public boolean isAdmin(){
        return isAuth && "Admin".equalsIgnoreCase(username);
    }
}
