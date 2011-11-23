package index;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import tools.normalizer.Normalizer;
import tools.weigher.Weigher;

public class TextIndexer extends Indexer {
	static private final String URL_MARK = "URL=";
	static private final String TITLE_MARK = "TITLE=";

	public void index(ArrayList<String> docsList, String encoding, Index index, Normalizer normalizer,
			boolean removeStopWords, Weigher weigher) throws IOException {
		Document document;
		String content;
		ArrayList<String> words;
		double weight;

		for (String fileName : docsList) {
			// On charge le document
			document = new Document();
			content = loadDocument(fileName, encoding, document);
			// et on l'ajoute à l'index.
			index.addDocument(document);

			// On normalise le contenu
			words = normalizer.normalize(content, removeStopWords);
			// et on ajoute les mots obtenus à l'index.
			for (String word : words) {
				index.addDocumentTerm(word, document);
			}
		}

		// On calcule la pondération des différents
		// termes dans les différents documents.
		for (String word : index.getTermsIndex()) {
			for (int idDocument : index.getDocumentsTerm(word)) {
				weight = weigher.calculateWeight(word, idDocument, index);
				index.setWeight(word, idDocument, weight);
				index.getDocument(idDocument).addWeight(weight);
			}
		}

		index.finalizeNorm();
	}

	/**
	 * Analyse un document texte et retourne son contenu indexable. Si l'URL et
	 * le titre de la page correspondante sont disponibles dans le document
	 * texte, l'objet document fourni est mis à jour en conséquence.
	 * 
	 * @param fileName
	 *            le nom du document à analyser
	 * @param encoding
	 *            l'encodage du fichier à analyser
	 * @param document
	 *            l'objet représentant le document devant être mis à jour si une
	 *            URL et un titre sont trouvés
	 * @return le contenu indexable du document
	 * @throws IOException
	 *             si la lecture du document échoue
	 */
	private String loadDocument(String fileName, String encoding, Document document) throws IOException {
		// On cherche la taille du fichier pour éviter d'avoir à réallouer le
		// contenu au fur et à mesure lors de la lecture du fichier.
		File file = new File(fileName);
		StringBuilder content = new StringBuilder((int) file.length());

		InputStream ips = new FileInputStream(file);
		InputStreamReader ipsr = new InputStreamReader(ips, encoding);
		BufferedReader br = new BufferedReader(ipsr);
		String line;
		// On parse l'url du document si présente
		if ((line = br.readLine()) != null) {
			if (line.startsWith(URL_MARK)) {
				line = line.substring(URL_MARK.length());
				document.setUrl(line);
			}
			content.append(line);
			content.append(" ");
		}
		// On parse le titre du document si présent
		if ((line = br.readLine()) != null) {
			if (line.startsWith(TITLE_MARK)) {
				line = line.substring(TITLE_MARK.length());
				document.setTitle(line);
			}
			content.append(line);
			content.append(" ");
		}
		// lecture de la fin du fichier texte
		while ((line = br.readLine()) != null) {
			content.append(line);
			content.append(" ");
		}
		br.close();
		ipsr.close();
		ips.close();

		return content.toString();
	}
}
