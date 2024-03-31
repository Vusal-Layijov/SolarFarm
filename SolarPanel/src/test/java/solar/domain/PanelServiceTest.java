package solar.domain;

import org.junit.jupiter.api.Test;
import solar.data.DataException;
import solar.data.PanelRepository;
import solar.data.PanelRepositoryTestDouble;
import solar.models.Material;
import solar.models.Panel;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PanelServiceTest {
    PanelRepository repositary = new PanelRepositoryTestDouble();
    PanelService service = new PanelService(repositary);
    @Test
    public void shouldFindAll() throws DataException{
        List<Panel> actual = service.findAll();
        assertEquals(3,actual.size());
    }
    @Test
    public void shouldFindExistingId() throws DataException {
        Panel panel = service.findById(1);
        assertNotNull(panel);
        assertEquals("Monocrystalline_silicon", panel.getMaterial().getDisplayText());
    }
    @Test
    public void shouldNotFindNonExistingId() throws DataException {
        Panel panel = service.findById(999);
        assertNull(panel);

    }
    @Test
    public void shouldFindBySection() throws DataException {
        List<Panel> actual = service.findBySection("A1");
        assertEquals(1,actual.size());
        assertEquals(2019, actual.get(0).getInstallationYear());
    }

//    add
    @Test
    public void shouldAddPanel() throws DataException{
        PanelResult actual = service.add(new Panel(0, "A1", 2, 11, 2020, Material.MONOCRYSTALLINE_SILICON, true));
        for (String message : actual.getMessages()){
            System.out.println(message);
        }
      assertEquals(99,actual.getPanel().getId());
    }
    @Test
    public void shouldNotAddNullPanel() throws DataException {
        PanelResult actual = service.add(null);
        assertFalse(actual.isSuccess());
        assertNull(actual.getPanel());
        assertEquals("Result can not be null",actual.getMessages().get(0));

    }
    @Test
    public void shouldNotAddNullSection() throws DataException {
        PanelResult actual = service.add(new Panel(0, "", 2, 11, 2020, Material.MONOCRYSTALLINE_SILICON, true));
        assertFalse(actual.isSuccess());
        assertNull(actual.getPanel());
        assertEquals("Section is required", actual.getMessages().get(0));
    }
    @Test
    public void shouldNotAddRowBetween0and250() throws DataException {
        PanelResult actual = service.add(new Panel(0, "A1", -1, 11, 2020, Material.MONOCRYSTALLINE_SILICON, true));
        PanelResult actual2 = service.add(new Panel(0, "A1", 251, 11, 2020, Material.MONOCRYSTALLINE_SILICON, true));
        assertFalse(actual.isSuccess());
        assertNull(actual.getPanel());
        assertEquals("Row must be between: [1, 250]", actual.getMessages().get(0));
        assertEquals("Row must be between: [1, 250]", actual2.getMessages().get(0));
    }

    @Test
    public void shouldNotAddColumnBetween0and250() throws DataException {
        PanelResult actual = service.add(new Panel(0, "A1", 11, -1, 2020, Material.MONOCRYSTALLINE_SILICON, true));
        PanelResult actual2 = service.add(new Panel(0, "A1", 241, 1000, 2020, Material.MONOCRYSTALLINE_SILICON, true));
        assertFalse(actual.isSuccess());
        assertNull(actual.getPanel());
        assertEquals("Column must be between: [1, 250]", actual.getMessages().get(0));
        assertEquals("Column must be between: [1, 250]", actual2.getMessages().get(0));

    }
    @Test
    public void shouldNotAddFutureYear() throws DataException {
        PanelResult actual = service.add(new Panel(0, "A1", 11, 5, 2025, Material.MONOCRYSTALLINE_SILICON, true));
        assertFalse(actual.isSuccess());
        assertNull(actual.getPanel());
        assertEquals("Year Installed must be in the past.", actual.getMessages().get(0));

    }
    @Test
    public void shouldNotAddEmptyMaterial() throws DataException {
        PanelResult actual = service.add(new Panel(0, "A1", 11, 5, 2024, null, true));
        assertFalse(actual.isSuccess());
        assertNull(actual.getPanel());
        assertEquals("Material is required and can only be one of the five materials: MULTICRYSTALLINE_SILICON, MONOCRYSTALLINE_SILICON, AMORPHOUS_SILICON, CADMIUM_TELLURIDE, COPPER_INDIUM_GALLIUM_SELENIDE", actual.getMessages().get(0));
    }
    @Test
    public void shouldNotAddNullTracking() throws DataException {
        PanelResult actual = service.add(new Panel(0, "A1", 11, 5, 2024, Material.MONOCRYSTALLINE_SILICON, true));
        assertTrue(actual.isSuccess());
        assertNotNull(actual.getPanel());
    }
    @Test
    public void shouldNotAddToAlreadyOccupiedSectionRowColumnCombination() throws DataException {
        PanelResult actual = service.add(new Panel(1, "A1", 5, 10, 2019, Material.MONOCRYSTALLINE_SILICON, true));
        assertFalse(actual.isSuccess());
        assertNull(actual.getPanel());
        assertEquals("This spot is already taken",actual.getMessages().get(0));
    }

    @Test
    public void shouldUpdateExistingPanel() throws DataException {
        List<Panel> all = service.findAll();
        Panel toUpdate=all.get(0);
        toUpdate.setSection("Test");
        PanelResult actual= service.update(toUpdate);
        assertTrue(actual.isSuccess());
        assertEquals(0,actual.getMessages().size());
        assertEquals("Test",all.get(0).getSection());
        assertEquals("Test", actual.getPanel().getSection());

    }
    @Test
    public void shouldNotUpdateNonExistingPanel() throws DataException {
    Panel panel = new Panel(0, "A1", 11, 1, 2020, Material.MONOCRYSTALLINE_SILICON, true);
    PanelResult actual = service.update(panel);
        assertFalse(actual.isSuccess());
        assertEquals(1,actual.getMessages().size());
        assertEquals("Panel with id: 0 does not exist",actual.getMessages().get(0));

    }
    @Test
    public void shouldDeleteExistingPanel() throws DataException {
        PanelResult actual   = service.deleteById(1);
        assertTrue(actual.isSuccess());

    }
    @Test
    public void shouldNotDeleteNonExistingPanel() throws DataException {
        PanelResult actual   = service.deleteById(999);
        assertFalse(actual.isSuccess());
        assertEquals(1,actual.getMessages().size());
    }


}
