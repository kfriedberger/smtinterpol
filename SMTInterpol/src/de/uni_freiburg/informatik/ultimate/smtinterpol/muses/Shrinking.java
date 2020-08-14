/*
 * Copyright (C) 2020 Leonard Fichtner
 *
 * This file is part of SMTInterpol.
 *
 * SMTInterpol is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * SMTInterpol is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with SMTInterpol.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.uni_freiburg.informatik.ultimate.smtinterpol.muses;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Random;

import de.uni_freiburg.informatik.ultimate.logic.SMTLIBException;
import de.uni_freiburg.informatik.ultimate.logic.Term;
import de.uni_freiburg.informatik.ultimate.smtinterpol.smtlib2.TerminationRequest;

/**
 * A class that provides methods for single MUS extraction.
 *
 * @author LeonardFichtner
 *
 */
public class Shrinking {

	/**
	 * Takes an boolean array representing an unsatisfiable set of constraints and a CritAdministrationSolver,
	 * containing all criticals found so far, to generate a minimal unsatisfiable subset. As a side effect, this method
	 * blocks all explored sets (also the found mus) in the map. The Random instance is used to make the shrinking
	 * randomly (the MUS that is created depends on the order in which the unknowns are tested to be critical). This
	 * method should only be used for Logics where checkSat cannot return LBool.UNKNOWN.
	 *
	 * @returns A MusContainer which contains the found mus and the corresponding proof of unsatisfiability. Returns
	 *          null, if termination was requested in the shrinking process.
	 */
	public static MusContainer shrink(final ConstraintAdministrationSolver solver, final BitSet workingConstraints,
			final UnexploredMap map, final TerminationRequest request, final Random rnd) throws SMTLIBException {
		solver.pushRecLevel();
		if (!MusUtils.contains(workingConstraints, solver.getCrits())) {
			throw new SMTLIBException("WorkingConstraints is corrupted! It should contain all crits.");
		}

		final BitSet unknown = (BitSet) workingConstraints.clone();
		unknown.andNot(solver.getCrits());

		final ArrayList<Integer> unknownPermutated = MusUtils.randomPermutation(unknown, rnd);
		int unknownIndex;
		while (!unknownPermutated.isEmpty()) {
			unknownIndex = unknownPermutated.get(unknownPermutated.size() - 1);
			unknownPermutated.remove(unknownPermutated.size() - 1);
			if (!unknown.get(unknownIndex)) {
				continue;
			}
			for (int j = unknown.nextSetBit(0); j >= 0; j = unknown.nextSetBit(j + 1)) {
				if (j == unknownIndex) {
					continue;
				}
				solver.assertUnknownConstraint(j);
			}
			switch (solver.checkSat()) {
			case UNSAT:
				unknown.clear(unknownIndex);
				final BitSet core = solver.getUnsatCore();
				unknown.and(core);
				solver.clearUnknownConstraints();
				break;
			case SAT:
				unknown.clear(unknownIndex);
				final BitSet extension = solver.getSatExtension(request);
				if (extension == null) {
					return null;
				}
				map.BlockDown(extension);
				solver.clearUnknownConstraints();
				solver.assertCriticalConstraint(unknownIndex);
				break;
			case UNKNOWN:
				if (request != null && request.isTerminationRequested()) {
					return null;
				}
				throw new SMTLIBException("Solver returns UNKNOWN in Shrinking process.");
			}
		}
		switch (solver.checkSat()) {
		case UNSAT:
			break;
		case SAT:
			throw new SMTLIBException("Something went wrong, the set of all crits should be unsatisfiable!!!");
		case UNKNOWN:
			if (request != null && request.isTerminationRequested()) {
				return null;
			}
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
	 * A variation of shrink, which does not listen to a TerminationRequest.
	 */
	public static MusContainer shrink(final ConstraintAdministrationSolver solver, final BitSet workingSet,
			final UnexploredMap map, final Random rnd) {
		return shrink(solver, workingSet, map, null, rnd);
	}
}
