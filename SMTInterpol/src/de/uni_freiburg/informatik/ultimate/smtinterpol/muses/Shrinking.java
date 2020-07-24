package de.uni_freiburg.informatik.ultimate.smtinterpol.muses;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Random;

import de.uni_freiburg.informatik.ultimate.logic.SMTLIBException;
import de.uni_freiburg.informatik.ultimate.logic.Term;

/**
 * A class that provides methods for single MUS extraction.
 *
 * @author LeonardFichtner (leonard.fichtner@web.de)
 *
 */
public class Shrinking {

	/**
	 * Takes an boolean array representing an unsatisfiable set of constraints and a CritAdministrationSolver,
	 * containing all criticals found so far, to generate a minimal unsatisfiable subset. The corresponding proof of
	 * unsatisfiability is returned. As a side effect, this method blocks all explored sets (also the found mus) in the
	 * map. This should only be used for Logics where checkSat cannot return LBool.UNKNOWN.
	 */
	public static MusContainer shrink(final ConstraintAdministrationSolver solver, final BitSet workingConstraints,
			final UnexploredMap map) {
		solver.pushRecLevel();
		if (!contains(workingConstraints, solver.getCrits())) {
			throw new SMTLIBException("WorkingConstraints is corrupted! It should contain all crits.");
		}

		final BitSet unknown = (BitSet) workingConstraints.clone();
		unknown.andNot(solver.getCrits());

		for (int i = unknown.nextSetBit(0); i >= 0; i = unknown.nextSetBit(i + 1)) {
			for (int j = unknown.nextSetBit(i + 1); j >= 0; j = unknown.nextSetBit(j + 1)) {
				solver.assertUnknownConstraint(j);
			}
			switch (solver.checkSat()) {
			case UNSAT:
				unknown.clear(i);
				final BitSet core = solver.getUnsatCore();
				unknown.and(core);
				solver.clearUnknownConstraints();
				break;
			case SAT:
				unknown.clear(i);
				solver.clearUnknownConstraints();
				final BitSet crits = solver.getCrits();
				crits.or(unknown);
				// Apply getExtension here
				map.BlockDown(crits);
				solver.assertCriticalConstraint(i);
				break;
			case UNKNOWN:
				throw new SMTLIBException("Solver returns UNKNOWN in Shrinking process.");
			}
		}
		switch (solver.checkSat()) {
		case UNSAT:
			break;
		case SAT:
			throw new SMTLIBException("Something went wrong, the set of all crits should be unsatisfiable!!!");
		case UNKNOWN:
			throw new SMTLIBException(
					"Solver returns UNKNOWN for set of all crits (despite of not doing it for a superset, weird).");
		}
		final Term proofOfMus = solver.getProof();
		solver.clearUnknownConstraints();
		final BitSet mus = solver.getCrits();
		map.BlockUp(mus);
		map.BlockDown(mus);
		solver.popRecLevel();
		return new MusContainer(mus, proofOfMus);
	}

	/**
	 * Check whether set1 contains set2.
	 */
	public static boolean contains(final BitSet set1, final BitSet set2) {
		final BitSet set2Clone = (BitSet) set2.clone();
		set2Clone.andNot(set1);
		return set2Clone.isEmpty();
	}

	/**
	 * Takes in a BitSet and returns an Array with the indices of the set Bits, but randomly permuted (pseudo-randomly,
	 * this method always uses the same seed).
	 */
	public static ArrayList<Integer> randomPermutation(final BitSet toBePermutated) {
		final ArrayList<Integer> toBePermutatedList = new ArrayList<>();
		for (int i = toBePermutated.nextSetBit(0); i >= 0; i = toBePermutated.nextSetBit(i + 1)) {
			toBePermutatedList.add(i);
		}
		java.util.Collections.shuffle(toBePermutatedList, new Random(1337));
		return toBePermutatedList;
	}
}