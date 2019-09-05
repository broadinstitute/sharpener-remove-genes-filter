package filter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import apimodels.Attribute;
import apimodels.GeneInfo;
import apimodels.Parameter;
import apimodels.Property;
import apimodels.TransformerInfo;
import apimodels.TransformerQuery;

public class RemoveGenesFilter {

	private static final String GENE_SYMBOL = "gene_symbol";
	private static final String TRANSFORMER_NAME = "Remove genes";
	private static final String REMOVE_GENES = "remove_genes";


	public static TransformerInfo transformerInfo() {
		TransformerInfo transformerInfo = new TransformerInfo().name(TRANSFORMER_NAME);
		transformerInfo.function(TransformerInfo.FunctionEnum.FILTER);
		transformerInfo.description("Remove-genes filter");
		transformerInfo.addParametersItem(new Parameter().name(REMOVE_GENES).type(Parameter.TypeEnum.STRING));
		transformerInfo.addRequiredAttributesItem(GENE_SYMBOL);
		return transformerInfo;
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
