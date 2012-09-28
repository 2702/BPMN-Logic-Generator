package pattern;

import graph.Marker;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

//TODO zrobic tak zeby czytal z pliku i ladnie obslugiwal na zasadzie {slug1} (moze byc bez klamer)
public class FormulaParser implements FormulaParserInterface {

	static Map<String, String> mFormulas = new HashMap<String, String>();

//	static {
//		mFormulas.put("Sequence", "(%1$s) => <>(%2$s)");
//		mFormulas.put("Exclusive-Choice", "(%1$s) => (<>(%2$s) & ~<>(%3$s))|(~<>(%2$s) & <>(%3$s))");
//		mFormulas.put("Parallel-Split", "(%1$s) => <>(%2$s) & <>(%3$s)");
//		mFormulas.put("Synchronization", "(%1$s) & (%2$s) => <>(%3$s)");
//		mFormulas.put("Simple-Merge", "(%1$s) | (%2$s) => <>(%3$s)");
//	}

	/** Reads pattern definitions from special file
	 * 
	 * @param filename
	 */
	public static void readPatternDefinitions(String filename) {
		File f = new File(filename);
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(f), Charset.forName("UTF-8")));
			String line;
			while ((line = br.readLine()) != null) {
				line = line.trim();
				if (line.startsWith("/*") || line.isEmpty()) {
					continue;
				} else {
					if (!line.endsWith(":")) {
						// other definition for current pattern; just skip it
						continue;
					}
					String prototype = line;
					String def = br.readLine();
					int leftBracketId = prototype.indexOf("(");
					int rightBracketId = prototype.lastIndexOf(")");
					String name = prototype.substring(0, leftBracketId);
					String args = prototype.substring(leftBracketId + 1, rightBracketId);
					String[] argnames = args.split(",");

					for (int i = 0; i < argnames.length; i++) {
						def = def.replace(argnames[i], "(%" + (i + 1) + "$s)");
					}
					mFormulas.put(name, def);
					System.out.println(line + "\n" + def + "\n" + name + "       " + argnames);

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public String getSequenceFormula(String formula1, String formula2) {
		return String.format(mFormulas.get("Sequence"), formula1, formula2);
	}

	@Override
	public String getSplitFormula(ArrayList<String> formulas, Marker marker) {
		if (marker.equals(Marker.EXCLUSIVE_CHOICE))
			return String.format(mFormulas.get("Exclusive-Choice"), formulas.toArray());
		if (marker.equals(Marker.PARALLEL_SPLIT))
			return String.format(mFormulas.get("Parallel-Split"), formulas.toArray());
		return null;
	}

	@Override
	public String getMergeFormula(ArrayList<String> formulas, Marker marker) {
		if (marker.equals(Marker.PARALLEL_SPLIT)) //Paralel_split dlatego, ze z drugiej strony sie tym rozdzielil
			return String.format(mFormulas.get("Synchronization"), formulas.toArray());
		if (marker.equals(Marker.EXCLUSIVE_CHOICE)) // na razie traktujemy to simple merge'em
			return String.format(mFormulas.get("Simple-Merge"), formulas.toArray());
		return null;
	}

}
