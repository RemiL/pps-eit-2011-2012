package searcher.extendedbooleanmodel;

import java.util.Set;

import index.Document;
import index.Index;
import searcher.InvalideQueryException;
import tools.normalizer.Normalizer;

/**
 * Classe abstraite repr�sentant une requ�te g�n�rique pour le mod�le bool�en
 * �tendu. Une requ�te peut �tre compos�e de sous-requ�tes imbriqu�es et de
 * termes simples.
 * 
 * Les requ�tes doivent �tre �crites de la fa�on suivante :
 * <ul>
 * <li>AND(req1, req2, ..., reqN)</li>
 * <li>OR(req1, req2, ..., reqN)</li>
 * <li>NOT(req1)</li>
 * </ul>
 * o� req sont des termes ou des sous-requ�tes. Dans le cas des requ�tes AND et
 * OR, il est possible d'utiliser la recherche par pr�fixe en utilisant la
 * notation "pr�fixe*" qui sera remplac�e par une requ�te OR contenant tous les
 * termes poss�dant le pr�fixe d�sir�. A la fin de la requ�te, il est possible
 * d'ajouter "| EXCLUDE(t1, t2, ...)" pour exclure strictement un certain nombre
 * de mots.
 */
public abstract class Query {
	/** Expression r�guli�re permant de d�tecter les requ�tes AND. */
	protected static final String AND = "AND\\(.+\\)";
	/** Expression r�guli�re permant de d�tecter les requ�tes OR. */
	protected static final String OR = "OR\\(.+\\)";
	/** Expression r�guli�re permant de d�tecter les requ�tes NOT. */
	protected static final String NOT = "NOT\\(.+\\)";
	/** Expression r�guli�re permant de d�tecter les requ�tes EXCLUDE. */
	protected static final String EXCLUDE = "EXCLUDE\\(.+\\)";
	/** Marqueur de pr�fixe. */
	protected static final String PREFIX_MARK = "*";

	/**
	 * Construit la liste des documents concern�s par la requ�te.
	 * 
	 * @param index
	 *            l'index sur lequel porte la requ�te.
	 * @param documents
	 *            la liste de documents � remplir.
	 */
	public abstract void getRelatedDocuments(Index index, Set<Document> documents);

	/**
	 * Calcule la similarit� d'un document de l'index fourni avec la requ�te.
	 * 
	 * @param index
	 *            l'index sur lequel porte la requ�te.
	 * @param document
	 *            la document dont on veut calculer la similarit� avec la
	 *            requ�te.
	 * @return la similarit� d'un document de l'index fourni avec la requ�te.
	 */
	public abstract double calculateSimilarity(Index index, Document document);

	/**
	 * Analyse une requ�te sous forme textuelle et construit l'arbre de requ�tes
	 * correspondant en utilisant le normalisateur fourni et en supprimant
	 * �ventuellement les mots vides.
	 * 
	 * @param queryString
	 *            la requ�te textuelle � analyser.
	 * @param normalizer
	 *            le normalisateur � utiliser.
	 * @param ignoreStopWords
	 *            indique si les mots vides doivent �tre ignor�s ou non.
	 * @return l'arbre de requ�tes correspondant � la requ�te textuelle fournie.
	 * @throws InvalideQueryException
	 *             si la requ�te est incorrectement formul�e.
	 * @throws EmptyQueryException
	 *             si la requ�te est vide.
	 */
	public static Query parse(String queryString, Normalizer normalizer, boolean ignoreStopWords)
			throws InvalideQueryException, EmptyQueryException {
		return parse(queryString, normalizer, ignoreStopWords, null);
	}

	/**
	 * Analyse une requ�te sous forme textuelle et construit l'arbre de requ�tes
	 * correspondant portant sur l'index fourni en utilisant le normalisateur
	 * fourni et en supprimant �ventuellement les mots vides.
	 * 
	 * @param queryString
	 *            la requ�te textuelle � analyser.
	 * @param normalizer
	 *            le normalisateur � utiliser.
	 * @param ignoreStopWords
	 *            indique si les mots vides doivent �tre ignor�s ou non.
	 * @param index
	 *            l'index sur lequel porte la requ�te.
	 * @return l'arbre de requ�tes correspondant � la requ�te textuelle fournie.
	 * @throws InvalideQueryException
	 *             si la requ�te est incorrectement formul�e.
	 * @throws EmptyQueryException
	 *             si la requ�te est vide.
	 */
	public static Query parse(String queryString, Normalizer normalizer, boolean ignoreStopWords, Index index)
			throws InvalideQueryException, EmptyQueryException {
		Query query;

		queryString = queryString.trim();

		if (queryString.matches(AND)) { // Si on d�tecte une requ�te AND
			query = new AndQuery(queryString.substring(4, queryString.length() - 1), normalizer, ignoreStopWords, index);
		} else if (queryString.matches(OR)) { // Si on d�tecte une requ�te OR
			query = new OrQuery(queryString.substring(3, queryString.length() - 1), normalizer, ignoreStopWords, index);
		} else if (queryString.matches(NOT)) { // Si on d�tecte une requ�te NOT
			query = new NotQuery(queryString.substring(4, queryString.length() - 1), normalizer, ignoreStopWords, index);
		} else if (index != null && queryString.endsWith(PREFIX_MARK)) {
			// Si on a un pr�fixe, on recherche les termes
			// correspondant dans l'index.
			Set<String> terms = index.getTermsIndex(queryString.substring(0, queryString.length() - 1));
			// On construit une requ�te OR pour le replacer.
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
