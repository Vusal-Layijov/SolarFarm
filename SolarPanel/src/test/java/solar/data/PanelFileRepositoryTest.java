package solar.data;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import solar.models.Material;
import solar.models.Panel;

import javax.xml.crypto.Data;
import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PanelFileRepositoryTest {
    private static final  String SEED_FILE_PATH="./data/panels-seed.csv";
    private static final  String TEST_FILE_PATH="./data/panels-test.csv";
    private final PanelFileRepository repository = new PanelFileRepository(TEST_FILE_PATH);

    @BeforeEach
    public void setUp() throws IOException {
        Path seedPath= Paths.get(SEED_FILE_PATH);
        Path testPath = Paths.get(TEST_FILE_PATH);

        Files.copy(seedPath, testPath, StandardCopyOption.REPLACE_EXISTING);

    }
    @Test
    public void shouldFindAll() throws DataException{
        List<Panel> actual = repository.findAll();
        assertEquals(5,actual.size());
        Panel panel = actual.get(0);
        assertEquals("A1", panel.getSection());

    }
    @Test
    public void shouldFindById() throws DataException{
        Panel actual = repository.findById(2);
        assertEquals("A2", actual.getSection());
        assertNotNull(actual);
    }

    @Test
    public void shouldNotFindById() throws DataException{
        Panel actual = repository.findById(20);
        assertNull(actual);
    }
    @Test
    public void shouldFindBySection () throws DataException {
        List<Panel> actual = repository.findBySection("A1");
        assertEquals(1,actual.size());
        Panel panel = actual.get(0);
        assertEquals("A1", panel.getSection());
    }
    @Test
    public void shouldAdd() throws DataException{
        Panel panel = new Panel(0, "A1", 5, 10, 2020, Material.MONOCRYSTALLINE_SILICON, true);
        Panel actual= repository.add(panel);
        assertEquals(6,actual.getId());
        assertEquals(6,repository.findAll().size());
    }
    @Test
    void goodIdGetsUpdated() throws DataException {
        Panel panel = repository.findById(1);

        panel.setSection("test section");
        boolean res = repository.update(panel);
        assertNotNull(panel);
        assertTrue(res);
    }
    @Test
    void badIdNotGetsUpdated() throws DataException {
        Panel panel = repository.findById(10);
        assertNull(panel);
    }

    @Test
    void shouldDelete() throws DataException {
        boolean res=repository.deleteById(1);
        assertTrue(res);
    }
    @Test
    void canNotDelete() throws DataException {
        boolean res=repository.deleteById(1000);
        assertFalse(res);
    }


}
