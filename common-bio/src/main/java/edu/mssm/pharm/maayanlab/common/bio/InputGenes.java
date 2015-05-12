/**
 * Handles validation of input genes
 * 
 * @author		Edward Y. Chen
 * @since		06/18/2013 
 */

package edu.mssm.pharm.maayanlab.common.bio;

import java.text.ParseException;
import java.util.Collection;
import java.util.regex.Pattern;

public class InputGenes {
	
	private static final Pattern crispPattern = Pattern.compile("[\\w\\-@./]+");

	public static boolean validateInputGenes(Collection<String> inputGenes) throws ParseException {
		if (inputGenes.isEmpty())
			throw new ParseException("Invalid input: Input list is empty.", -1);
		
		int i = 0;
		for (String gene : inputGenes) {
			if (!crispPattern.matcher(gene).matches()) {
				// Point out which gene and line it is
				throw new ParseException("Invalid input: " + gene + " at line " + (i+1) + " is not a valid Entrez Gene Symbol.", i);
			}
			i++;
		}
		return true;
	}
	
	public static boolean validateFuzzyInputGenes(Collection<String> inputGenes) throws ParseException {
		if (inputGenes.isEmpty())
			throw new ParseException("", -1);
		
		int i = 0;
		for (String gene : inputGenes) {
			String[] splitGene = gene.split(",");
			if (splitGene.length < 2) {
				// If cannot split, then not fuzzy
				throw new ParseException("Invalid input: " + gene + " at line " + (i+1) + " is not a fuzzy input.", i);
			}
			
			if (!crispPattern.matcher(splitGene[0]).matches()) {
				// Point out which gene and line it is
				throw new ParseException("Invalid input: " + splitGene[0] + " at line " + (i+1) + " is not a valid Entrez Gene Symbol.", i);
			}
			
			try {
				double degreeOfMembership = Double.parseDouble(splitGene[1]);
				if (degreeOfMembership < -1 || degreeOfMembership > 1) {
					// Point out which degree of membership is out of bounds
					throw new NumberFormatException();
				}
			} catch (NumberFormatException nfe) {
				throw new ParseException("Invalid input: " + splitGene[0] + " with a degree of membership of " + splitGene[1] + " at line " + (i+1) + " is out of bounds. Membership must be between -1 and 1 (inclusive)", i);
			}
			i++;
		}
		return true;
	}
	
	public static boolean isFuzzy(Collection<String> inputGenes) {
		for (String gene : inputGenes) {
			if (!gene.contains(","))	// Switch to fuzzy processing if line contains a comma
				return false;
		}
		
		return true;
	}
}
