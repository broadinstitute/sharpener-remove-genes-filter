package filter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;

import apimodels.Attribute;
import apimodels.GeneInfo;
import apimodels.Property;
import apimodels.TransformerInfo;
import apimodels.TransformerQuery;

public class RemoveGenesFilter {

	private static final String GENE_SYMBOL = "gene_symbol";
	private static String REMOVE_GENES = "gene symbols";

	private static ObjectMapper mapper = new ObjectMapper();

	static {
		mapper.setSerializationInclusion(Include.NON_NULL);
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	}


	public static TransformerInfo transformerInfo() {
		try {
			String json = new String(Files.readAllBytes(Paths.get("transformer_info.json")));
			TransformerInfo info = mapper.readValue(json, TransformerInfo.class);
			REMOVE_GENES = info.getParameters().get(0).getName();
			return info;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}


	public static ArrayList<GeneInfo> filter(TransformerQuery query) {
		ArrayList<GeneInfo> genes = new ArrayList<GeneInfo>();
		Set<String> removeGenes = genesToRemove(query);
		for (GeneInfo gene : query.getGenes()) {
			String symbol = symbol(gene);
			if (symbol == null || !removeGenes.contains(symbol)) {
				genes.add(gene);
			}
		}
		return genes;
	}


	private static Set<String> genesToRemove(TransformerQuery query) {
		Set<String> genes = new HashSet<String>();
		for (Property property : query.getControls()) {
			if (REMOVE_GENES.equals(property.getName())) {
				for (String gene : property.getValue().split(",")) {
					genes.add(gene.trim());
				}
			}
		}
		return genes;
	}


	private static String symbol(GeneInfo gene) {
		for (Attribute attribute : gene.getAttributes()) {
			if (GENE_SYMBOL.equals(attribute.getName())) {
				return attribute.getValue();
			}
		}
		return null;
	}
}
