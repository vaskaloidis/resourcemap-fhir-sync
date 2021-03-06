package resourcemap;

import com.apelon.config.Config;
import com.apelon.config.ResourceMapData;
import com.apelon.fhir.FhirData;
import com.apelon.fhir.objects.FhirValue;
import com.apelon.http.HttpMessageSender;
import com.apelon.resourcemap.ResourcemapCommandBuilders;
import com.apelon.resourcemap.ResourcemapHandler;
import com.apelon.config.ResourceMapData;
import com.apelon.resourcemap.objects.ResourcemapField;
import com.apelon.resourcemap.objects.ResourcemapLayer;
import com.apelon.util.TestLogger;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;


public class update_facility_region_layer_test {
	public static final Logger logger = TestLogger.get();

	@Test
	public void main() {
		ResourcemapLayer layer = new ResourcemapLayer();
		layer.setCollectionId(Config.getResourcemapCollectionTanzania());
		layer.setLayerId(1816);
		layer.setLayerName("Geography"); //Medical Facility Information
		layer.setLayerOrder(1);

		ResourcemapField field = new ResourcemapField();
		field.setFieldCode("regions");
		field.setFieldName("regions");
		field.setFieldOrder(1);
		field.setFieldId(14413);
		field.setValuesetName(Config.getValuesetTanzaniaRegions());
		field.setNextId(2);
		layer.setUpdateField(field);

		//field.setFieldId(14410);

		//Dummy Data or FHIR Data
		List<FhirValue> fhirData = FhirData.getValueset(Config.getValuesetTanzaniaRegions());
		//List<FhirValue> fhirData = FhirData.getTestValueset(10);

		//Build JSON Command
		ResourcemapCommandBuilders rmcb = new ResourcemapCommandBuilders();
		String command = rmcb.buildUpdateLayerPayload(layer, fhirData);

		//Get Resource Map API Endpoint
		String endpoint = Config.getResourcemapApiCollectionsUpdateUrl(layer.getCollectionId(), layer.getLayerId());

		//Execute Command
		boolean status = HttpMessageSender.executePut(endpoint, command);

		assertTrue(status);

		logger.debug(command);
	}

}
