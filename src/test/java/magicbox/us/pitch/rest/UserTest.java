package magicbox.us.pitch.rest;

import magicbox.us.pitch.model.User;
import magicbox.us.pitch.model.UserBuilder;
import org.junit.Test;

public class UserTest {

    @Test
    public void testUserBuilder() throws Exception {
        User u = new UserBuilder()
                    .name("test")
                    .email("test@test.com")
                    .skills("java")
                    .password("123")
                    .buildUser();

        assert u!=null;
    }

    @Test(expected = Exception.class)
    public void testWrongEmail() throws Exception {
         new UserBuilder()
            .name("test")
            .email("test.com")
            .password("123")
            .buildUser();
    }

    @Test(expected = Exception.class)
    public void testMissingParam() throws Exception {
        new UserBuilder()
                .password("123")
                .buildUser();
    }
}
