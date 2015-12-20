package magicbox.us.pitch.rest;

import magicbox.us.pitch.model.PitchBuilder;
import magicbox.us.pitch.model.PitchEntity;
import org.junit.Test;

public class PitchTest {

    @Test
    public void testPitch() throws Exception {
        PitchEntity p = new PitchBuilder()
                .email("test@test.com")
                .tag("java")
                .title("Java presentation")
                .buildPitch();

        assert  p !=null;
    }

    @Test(expected = Exception.class)
    public void testMissintParam() throws Exception {
        PitchEntity p = new PitchBuilder()
                .email("test@test.com")
                .buildPitch();
    }

    @Test(expected = Exception.class)
    public void testWrongEmail() throws Exception {
        new PitchBuilder()
                .email("test.com")
                .buildPitch();
    }

    @Test(expected = Exception.class)
    public void testWrongPath() throws Exception {
        new PitchBuilder()
                .videourl("test")
                .buildPitch();
    }
}