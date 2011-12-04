package searcher.extendedbooleanmodel;

import java.util.Set;

import index.Document;
import index.Index;
import searcher.InvalideQueryException;
import tools.normalizer.Normalizer;

/**
 * Classe abstraite représentant une requête générique pour le modèle booléen
 * étendu. Une requête peut être composée de sous-requêtes imbriquées et de
 * termes simples.
 * 
 * Les requêtes doivent être écrites de la façon suivante :
 * <ul>
 * <li>AND(req1, req2, ..., reqN)</li>
 * <li>OR(req1, req2, ..., reqN)</li>
 * <li>NOT(req1)</li>
 * </ul>
 * où req sont des termes ou des sous-requêtes. Dans le cas des requêtes AND et
 * OR, il est possible d'utiliser la recherche par préfixe en utilisant la
 * notation "préfixe*" qui sera remplacée par une requête OR contenant tous les
 * termes possédant le préfixe désiré. A la fin de la requête, il est possible
 * d'ajouter "| EXCLUDE(t1, t2, ...)" pour exclure strictement un certain nombre
 * de mots.
 */
public abstract class Query {
	/** Expression régulière permant de détecter les requêtes AND. */
	protected static final String AND = "AND\\(.+\\)";
	/** Expression régulière permant de détecter les requêtes OR. */
	protected static final String OR = "OR\\(.+\\)";
	/** Expression régulière permant de détecter les requêtes NOT. */
	protected static final String NOT = "NOT\\(.+\\)";
	/** Expression régulière permant de détecter les requêtes EXCLUDE. */
	protected static final String EXCLUDE = "EXCLUDE\\(.+\\)";
	/** Marqueur de préfixe. */
	protected static final String PREFIX_MARK = "*";

	/**
	 * Construit la liste des documents concernés par la requête.
	 * 
	 * @param index
	 *            l'index sur lequel porte la requête.
	 * @param documents
	 *            la liste de documents à remplir.
	 */
	public abstract void getRelatedDocuments(Index index, Set<Document> documents);

	/**
	 * Calcule la similarité d'un document de l'index fourni avec la requête.
	 * 
	 * @param index
	 *            l'index sur lequel porte la requête.
	 * @param document
	 *            la document dont on veut calculer la similarité avec la
	 *            requête.
	 * @return la similarité d'un document de l'index fourni avec la requête.
	 */
	public abstract double calculateSimilarity(Index index, Document document);

	/**
	 * Analyse une requête sous forme textuelle et construit l'arbre de requêtes
	 * correspondant en utilisant le normalisateur fourni et en supprimant
	 * éventuellement les mots vides.
	 * 
	 * @param queryString
	 *            la requête textuelle à analyser.
	 * @param normalizer
	 *            le normalisateur à utiliser.
	 * @param ignoreStopWords
	 *            indique si les mots vides doivent être ignorés ou non.
	 * @return l'arbre de requêtes correspondant à la requête textuelle fournie.
	 * @throws InvalideQueryException
	 *             si la requête est incorrectement formulée.
	 * @throws EmptyQueryException
	 *             si la requête est vide.
	 */
	public static Query parse(String queryString, Normalizer normalizer, boolean ignoreStopWords)
			throws InvalideQueryException, EmptyQueryException {
		return parse(queryString, normalizer, ignoreStopWords, null);
	}

	/**
	 * Analyse une requête sous forme textuelle et construit l'arbre de requêtes
	 * correspondant portant sur l'index fourni en utilisant le normalisateur
	 * fourni et en supprimant éventuellement les mots vides.
	 * 
	 * @param queryString
	 *            la requête textuelle à analyser.
	 * @param normalizer
	 *            le normalisateur à utiliser.
	 * @param ignoreStopWords
	 *            indique si les mots vides doivent être ignorés ou non.
	 * @param index
	 *            l'index sur lequel porte la requête.
	 * @return l'arbre de requêtes correspondant à la requête textuelle fournie.
	 * @throws InvalideQueryException
	 *             si la requête est incorrectement formulée.
	 * @throws EmptyQueryException
	 *             si la requête est vide.
	 */
	public static Query parse(String queryString, Normalizer normalizer, boolean ignoreStopWords, Index index)
			throws InvalideQueryException, EmptyQueryException {
		Query query;

		queryString = queryString.trim();

		if (queryString.matches(AND)) { // Si on détecte une requête AND
			query = new AndQuery(queryString.substring(4, queryString.length() - 1), normalizer, ignoreStopWords, index);
		} else if (queryString.matches(OR)) { // Si on détecte une requête OR
			query = new OrQuery(queryString.substring(3, queryString.length() - 1), normalizer, ignoreStopWords, index);
		} else if (queryString.matches(NOT)) { // Si on détecte une requête NOT
			query = new NotQuery(queryString.substring(4, queryString.length() - 1), normalizer, ignoreStopWords, index);
		} else if (index != null && queryString.endsWith(PREFIX_MARK)) {
			// Si on a un préfixe, on recherche les termes
			// correspondant dans l'index.
			Set<String> terms = index.getTermsIndex(queryString.substring(0, queryString.length() - 1));
			// On construit une requête OR pour le replacer.
			StringBuilder replacementQuery = new StringBuilder("OR(");
			int i = 1;
			for (String term : terms) {
				replacementQuery.append(term);
				if (i < terms.size())
					replacementQuery.append(", ");
				i++;
			}
			replacementQuery.append(")");
			System.out.println(replacementQuery.toString());
			query = new OrQuery(replacementQuery.toString(), normalizer, ignoreStopWords, index);
		} else { // Sinon on doit avoir un terme simple
			query = new QueryTerm(queryString, normalizer, ignoreStopWords);
		}

		return query;
	}
}
