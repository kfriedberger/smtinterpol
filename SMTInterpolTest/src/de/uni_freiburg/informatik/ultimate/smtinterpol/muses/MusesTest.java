package de.uni_freiburg.informatik.ultimate.smtinterpol.muses;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Random;

import org.junit.Assert;
import org.junit.Test;

import de.uni_freiburg.informatik.ultimate.logic.AnnotatedTerm;
import de.uni_freiburg.informatik.ultimate.logic.Annotation;
import de.uni_freiburg.informatik.ultimate.logic.Logics;
import de.uni_freiburg.informatik.ultimate.logic.SMTLIBException;
import de.uni_freiburg.informatik.ultimate.logic.Script;
import de.uni_freiburg.informatik.ultimate.logic.Script.LBool;
import de.uni_freiburg.informatik.ultimate.logic.Sort;
import de.uni_freiburg.informatik.ultimate.logic.Term;
import de.uni_freiburg.informatik.ultimate.smtinterpol.DefaultLogger;
import de.uni_freiburg.informatik.ultimate.smtinterpol.dpll.DPLLEngine;
import de.uni_freiburg.informatik.ultimate.smtinterpol.dpll.NamedAtom;
import de.uni_freiburg.informatik.ultimate.smtinterpol.smtlib2.SMTInterpol;
import de.uni_freiburg.informatik.ultimate.smtinterpol.smtlib2.TerminationRequest;

/**
 * Tests for everything that has to do with MUSes.
 *
 * @author LeonardFichtner
 *
 */
public class MusesTest {

	private Script setupScript(final Logics logic) {
		final Script script = new SMTInterpol();
		script.setOption(":produce-models", true);
		script.setOption(":produce-proofs", true);
		script.setOption(":interactive-mode", true);
		script.setOption(":produce-unsat-cores", true);
		script.setLogic(logic);
		return script;
	}

	private Script setupScript(final Logics logic, final TerminationRequest request) {
		final Script script = new SMTInterpol(request);
		script.setOption(":produce-models", true);
		script.setOption(":produce-proofs", true);
		script.setOption(":interactive-mode", true);
		script.setOption(":produce-unsat-cores", true);
		script.setLogic(logic);
		return script;
	}

	/**
	 * Note that the constraints should be declared in the solver, but no constraints should be asserted!
	 */
	private void checkWhetherSetIsMus(final BitSet supposedMus, final ConstraintAdministrationSolver solver) {
		checkUnsat(supposedMus, solver);
		checkMinimality(supposedMus, solver);
	}

	/**
	 * Note that the constraints should be declared in the solver, but no constraints should be asserted!
	 */
	private void checkUnsat(final BitSet supposedMus, final ConstraintAdministrationSolver solver) {
		solver.pushRecLevel();
		for (int i = supposedMus.nextSetBit(0); i >= 0; i = supposedMus.nextSetBit(i + 1)) {
			solver.assertCriticalConstraint(i);
		}
		Assert.assertTrue(solver.checkSat() == LBool.UNSAT);
		solver.popRecLevel();
	}

	/**
	 * Note that the constraints should be declared in the solver, but no constraints should be asserted!
	 */
	private void checkMinimality(final BitSet supposedMus, final ConstraintAdministrationSolver solver) {
		solver.pushRecLevel();
		for (int i = supposedMus.nextSetBit(0); i >= 0; i = supposedMus.nextSetBit(i + 1)) {
			solver.pushRecLevel();
			for (int j = supposedMus.nextSetBit(0); j >= 0; j = supposedMus.nextSetBit(j + 1)) {
				if (i == j) {
					continue;
				}
				solver.assertCriticalConstraint(i);
			}
			Assert.assertTrue(solver.checkSat() == LBool.SAT);
			solver.popRecLevel();
		}
		solver.popRecLevel();
	}

	private void setupUnsatSet1(final Script script, final Translator translator, final DPLLEngine engine) {
		final ArrayList<String> names = new ArrayList<>();
		final ArrayList<Annotation> annots = new ArrayList<>();
		for (int i = 0; i < 5; i++) {
			names.add("c" + String.valueOf(i));
		}
		for (int i = 0; i < names.size(); i++) {
			annots.add(new Annotation(":named", names.get(i)));
		}
		final Sort intSort = script.sort("Int");
		script.declareFun("x", Script.EMPTY_SORT_ARRAY, intSort);
		script.declareFun("y", Script.EMPTY_SORT_ARRAY, intSort);
		script.declareFun("z", Script.EMPTY_SORT_ARRAY, intSort);
		final Term x = script.term("x");
		final Term y = script.term("y");
		final Term z = script.term("z");
		final Term c0 = script.term(">=", x, script.numeral("30"));
		final Term c1 = script.term(">=", x, script.numeral("101"));
		final Term c2 = script.term("<", x, z);
		final Term c3 = script.term("<=", z, script.numeral("101"));
		final Term c4 = script.term("=", y, script.numeral("2"));
		declareConstraint(script, translator, engine, c0, annots.get(0));
		declareConstraint(script, translator, engine, c1, annots.get(1));
		declareConstraint(script, translator, engine, c2, annots.get(2));
		declareConstraint(script, translator, engine, c3, annots.get(3));
		declareConstraint(script, translator, engine, c4, annots.get(4));
	}

	private void setupUnsatSet2(final Script script, final Translator translator, final DPLLEngine engine) {
		final ArrayList<String> names = new ArrayList<>();
		final ArrayList<Annotation> annots = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			names.add("c" + String.valueOf(i));
		}
		for (int i = 0; i < names.size(); i++) {
			annots.add(new Annotation(":named", names.get(i)));
		}
		final Sort intSort = script.sort("Int");
		script.declareFun("x", Script.EMPTY_SORT_ARRAY, intSort);
		script.declareFun("y", Script.EMPTY_SORT_ARRAY, intSort);
		script.declareFun("z", Script.EMPTY_SORT_ARRAY, intSort);
		final Term x = script.term("x");
		final Term y = script.term("y");
		final Term z = script.term("z");
		final Term c0 = script.term("=", x, script.numeral("53"));
		final Term c1 = script.term(">", x, script.numeral("23"));
		final Term c2 = script.term("<", x, z);
		final Term c3 = script.term("=", x, script.numeral("535"));
		final Term c4 = script.term("=", y, script.numeral("1234"));
		final Term c5 = script.term("<=", z, script.numeral("23"));
		final Term c6 = script.term(">=", x, script.numeral("34"));
		final Term c7 = script.term("=", y, script.numeral("4321"));
		final Term c8 = script.term("=", x, script.numeral("23"));
		final Term c9 = script.term("=", x, script.numeral("5353"));
		declareConstraint(script, translator, engine, c0, annots.get(0));
		declareConstraint(script, translator, engine, c1, annots.get(1));
		declareConstraint(script, translator, engine, c2, annots.get(2));
		declareConstraint(script, translator, engine, c3, annots.get(3));
		declareConstraint(script, translator, engine, c4, annots.get(4));
		declareConstraint(script, translator, engine, c5, annots.get(5));
		declareConstraint(script, translator, engine, c6, annots.get(6));
		declareConstraint(script, translator, engine, c7, annots.get(7));
		declareConstraint(script, translator, engine, c8, annots.get(8));
		declareConstraint(script, translator, engine, c9, annots.get(9));
	}

	private void setupUnsatSet3(final Script script, final Translator translator, final DPLLEngine engine) {
		final ArrayList<String> names = new ArrayList<>();
		final ArrayList<Annotation> annots = new ArrayList<>();
		for (int i = 0; i < 3; i++) {
			names.add("c" + String.valueOf(i));
		}
		for (int i = 0; i < names.size(); i++) {
			annots.add(new Annotation(":named", names.get(i)));
		}
		final Sort intSort = script.sort("Int");
		script.declareFun("x", Script.EMPTY_SORT_ARRAY, intSort);
		script.declareFun("z", Script.EMPTY_SORT_ARRAY, intSort);
		final Term x = script.term("x");
		final Term z = script.term("z");
		final Term c0 = script.term(">=", x, script.numeral("101"));
		final Term c1 = script.term("<", x, z);
		final Term c2 = script.term("<=", z, script.numeral("101"));
		declareConstraint(script, translator, engine, c0, annots.get(0));
		declareConstraint(script, translator, engine, c1, annots.get(1));
		declareConstraint(script, translator, engine, c2, annots.get(2));
	}

	/**
	 * This is really just unsat set 2, but with different order.
	 */
	private void setupUnsatSet4(final Script script, final Translator translator, final DPLLEngine engine) {
		final ArrayList<String> names = new ArrayList<>();
		final ArrayList<Annotation> annots = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			names.add("c" + String.valueOf(i));
		}
		for (int i = 0; i < names.size(); i++) {
			annots.add(new Annotation(":named", names.get(i)));
		}
		final Sort intSort = script.sort("Int");
		script.declareFun("x", Script.EMPTY_SORT_ARRAY, intSort);
		script.declareFun("y", Script.EMPTY_SORT_ARRAY, intSort);
		script.declareFun("z", Script.EMPTY_SORT_ARRAY, intSort);
		final Term x = script.term("x");
		final Term y = script.term("y");
		final Term z = script.term("z");
		final Term c0 = script.term(">", x, script.numeral("23"));
		final Term c1 = script.term("=", x, script.numeral("5353"));
		final Term c2 = script.term("<", x, z);
		final Term c3 = script.term("=", x, script.numeral("535"));
		final Term c4 = script.term("=", y, script.numeral("1234"));
		final Term c5 = script.term("=", x, script.numeral("53"));
		final Term c6 = script.term(">=", x, script.numeral("34"));
		final Term c7 = script.term("=", y, script.numeral("4321"));
		final Term c8 = script.term("=", x, script.numeral("23"));
		final Term c9 = script.term("<=", z, script.numeral("23"));
		declareConstraint(script, translator, engine, c0, annots.get(0));
		declareConstraint(script, translator, engine, c1, annots.get(1));
		declareConstraint(script, translator, engine, c2, annots.get(2));
		declareConstraint(script, translator, engine, c3, annots.get(3));
		declareConstraint(script, translator, engine, c4, annots.get(4));
		declareConstraint(script, translator, engine, c5, annots.get(5));
		declareConstraint(script, translator, engine, c6, annots.get(6));
		declareConstraint(script, translator, engine, c7, annots.get(7));
		declareConstraint(script, translator, engine, c8, annots.get(8));
		declareConstraint(script, translator, engine, c9, annots.get(9));
	}

	private void setupUnsatSet5(final Script script, final Translator translator, final DPLLEngine engine) {
		final ArrayList<String> names = new ArrayList<>();
		final ArrayList<Annotation> annots = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			names.add("c" + String.valueOf(i));
		}
		for (int i = 0; i < names.size(); i++) {
			annots.add(new Annotation(":named", names.get(i)));
		}
		final Sort intSort = script.sort("Int");
		script.declareFun("v", Script.EMPTY_SORT_ARRAY, intSort);
		script.declareFun("w", Script.EMPTY_SORT_ARRAY, intSort);
		script.declareFun("x", Script.EMPTY_SORT_ARRAY, intSort);
		script.declareFun("y", Script.EMPTY_SORT_ARRAY, intSort);
		script.declareFun("z", Script.EMPTY_SORT_ARRAY, intSort);
		final Term v = script.term("v");
		final Term w = script.term("w");
		final Term x = script.term("x");
		final Term y = script.term("y");
		final Term z = script.term("z");
		final Term c0 = script.term("<", v, w);
		final Term c1 = script.term("<", w, x);
		final Term c2 = script.term("<", x, y);
		final Term c3 = script.term("<", y, z);
		final Term c4 = script.term("=", v, script.numeral("2000"));
		final Term c5 = script.term("=", z, script.numeral("5"));
		final Term c6 = script.term("=", x, script.numeral("1000"));
		final Term c7 = script.term("=", x, script.numeral("1001"));
		final Term c8 = script.term("=", w, script.numeral("1500"));
		final Term c9 = script.term("=", y, script.numeral("100"));
		declareConstraint(script, translator, engine, c0, annots.get(0));
		declareConstraint(script, translator, engine, c1, annots.get(1));
		declareConstraint(script, translator, engine, c2, annots.get(2));
		declareConstraint(script, translator, engine, c3, annots.get(3));
		declareConstraint(script, translator, engine, c4, annots.get(4));
		declareConstraint(script, translator, engine, c5, annots.get(5));
		declareConstraint(script, translator, engine, c6, annots.get(6));
		declareConstraint(script, translator, engine, c7, annots.get(7));
		declareConstraint(script, translator, engine, c8, annots.get(8));
		declareConstraint(script, translator, engine, c9, annots.get(9));
	}

	private void declareConstraint(final Script script, final Translator translator, final DPLLEngine engine,
			final Term constraint, final Annotation... annotation) throws SMTLIBException {
		final AnnotatedTerm annotatedConstraint = (AnnotatedTerm) script.annotate(constraint, annotation);
		final NamedAtom atom = new NamedAtom(annotatedConstraint, 0);
		atom.setPreferredStatus(atom.getAtom());
		atom.lockPreferredStatus();
		engine.addAtom(atom);
		translator.declareConstraint(atom);
	}

	@Test
	public void testShrinkerNormal() {
		final Script script = setupScript(Logics.ALL);
		final DPLLEngine engine = new DPLLEngine(new DefaultLogger(), null);
		final Translator translator = new Translator();
		setupUnsatSet2(script, translator, engine);

		final UnexploredMap map = new UnexploredMap(engine, translator);
		final ConstraintAdministrationSolver solver = new ConstraintAdministrationSolver(script, translator);
		final BitSet workingSet = new BitSet();
		workingSet.flip(0, 10);
		final MusContainer container = Shrinking.shrink(solver, workingSet, map);

		System.out.println("Shrinker returned: " + container.getMus().toString());
		checkWhetherSetIsMus(container.getMus(), solver);
	}

	@Test
	public void testShrinkerRestrictedSet() {
		final Script script = setupScript(Logics.ALL);
		final DPLLEngine engine = new DPLLEngine(new DefaultLogger(), null);
		final Translator translator = new Translator();
		setupUnsatSet2(script, translator, engine);

		final UnexploredMap map = new UnexploredMap(engine, translator);
		final ConstraintAdministrationSolver solver = new ConstraintAdministrationSolver(script, translator);
		final BitSet workingSet = new BitSet();
		workingSet.set(0);
		workingSet.set(1);
		workingSet.set(3);
		workingSet.set(8);
		workingSet.set(9);
		workingSet.flip(0, 10);
		final MusContainer container = Shrinking.shrink(solver, workingSet, map);

		System.out.println("Shrinker returned: " + container.getMus().toString());
		checkWhetherSetIsMus(container.getMus(), solver);
	}

	@Test
	public void testShrinkerWorkingSetIsMus() {
		final Script script = setupScript(Logics.ALL);
		final DPLLEngine engine = new DPLLEngine(new DefaultLogger(), null);
		final Translator translator = new Translator();
		setupUnsatSet2(script, translator, engine);

		final UnexploredMap map = new UnexploredMap(engine, translator);
		final ConstraintAdministrationSolver solver = new ConstraintAdministrationSolver(script, translator);
		final BitSet workingSet = new BitSet();
		workingSet.set(1);
		workingSet.set(2);
		workingSet.set(5);
		final MusContainer container = Shrinking.shrink(solver, workingSet, map);

		System.out.println("Shrinker returned: " + container.getMus().toString());
		checkWhetherSetIsMus(container.getMus(), solver);
	}

	@Test
	public void testShrinkerMusAssertedBefore() {
		final Script script = setupScript(Logics.ALL);
		final DPLLEngine engine = new DPLLEngine(new DefaultLogger(), null);
		final Translator translator = new Translator();
		setupUnsatSet2(script, translator, engine);

		final UnexploredMap map = new UnexploredMap(engine, translator);
		final ConstraintAdministrationSolver solver = new ConstraintAdministrationSolver(script, translator);
		solver.pushRecLevel();
		solver.assertCriticalConstraint(4);
		solver.assertCriticalConstraint(7);
		final BitSet workingSet = new BitSet();
		workingSet.set(1);
		workingSet.set(2);
		workingSet.set(4);
		workingSet.set(7);
		final MusContainer container = Shrinking.shrink(solver, workingSet, map);

		System.out.println("Shrinker returned: " + container.getMus().toString());
		Assert.assertTrue(solver.checkSat() == LBool.UNSAT);
		solver.popRecLevel();
		checkWhetherSetIsMus(container.getMus(), solver);
	}

	@Test
	public void testShrinkerSatSetAssertedBefore() {
		final Script script = setupScript(Logics.ALL);
		final DPLLEngine engine = new DPLLEngine(new DefaultLogger(), null);
		final Translator translator = new Translator();
		setupUnsatSet2(script, translator, engine);

		final ConstraintAdministrationSolver solver = new ConstraintAdministrationSolver(script, translator);
		final UnexploredMap map = new UnexploredMap(engine, translator);
		solver.pushRecLevel();
		solver.assertCriticalConstraint(1);
		solver.assertCriticalConstraint(2);
		final BitSet workingSet = new BitSet();
		workingSet.set(5);
		workingSet.set(1);
		workingSet.set(2);
		final MusContainer container = Shrinking.shrink(solver, workingSet, map);

		System.out.println("Shrinker returned: " + container.getMus().toString());
		solver.popRecLevel();
		checkWhetherSetIsMus(container.getMus(), solver);
		Assert.assertTrue(solver.checkSat() == LBool.SAT);
	}

	@Test(expected = SMTLIBException.class)
	public void testShrinkerWorkingSetDoesNotContainCrits() {
		final Script script = setupScript(Logics.ALL);
		final DPLLEngine engine = new DPLLEngine(new DefaultLogger(), null);
		final Translator translator = new Translator();
		setupUnsatSet2(script, translator, engine);

		final ConstraintAdministrationSolver solver = new ConstraintAdministrationSolver(script, translator);
		final UnexploredMap map = new UnexploredMap(engine, translator);
		solver.pushRecLevel();
		solver.assertCriticalConstraint(1);
		solver.assertCriticalConstraint(2);
		final BitSet workingSet = new BitSet();
		workingSet.set(5);
		final MusContainer container = Shrinking.shrink(solver, workingSet, map);

		System.out.println("Shrinker returned: " + container.getMus().toString());
		solver.popRecLevel();
		checkWhetherSetIsMus(container.getMus(), solver);
		Assert.assertTrue(solver.checkSat() == LBool.SAT);
	}

	@Test(expected = SMTLIBException.class)
	public void testShrinkerEmptySet() {
		final Script script = setupScript(Logics.ALL);
		final DPLLEngine engine = new DPLLEngine(new DefaultLogger(), null);
		final Translator translator = new Translator();
		setupUnsatSet2(script, translator, engine);

		final ConstraintAdministrationSolver solver = new ConstraintAdministrationSolver(script, translator);
		final UnexploredMap map = new UnexploredMap(engine, translator);
		final BitSet workingSet = new BitSet();
		Shrinking.shrink(solver, workingSet, map);
	}

	@Test(expected = SMTLIBException.class)
	public void testShrinkerSatSet() {
		final Script script = setupScript(Logics.ALL);
		final DPLLEngine engine = new DPLLEngine(new DefaultLogger(), null);
		final Translator translator = new Translator();
		setupUnsatSet2(script, translator, engine);

		final ConstraintAdministrationSolver solver = new ConstraintAdministrationSolver(script, translator);
		final UnexploredMap map = new UnexploredMap(engine, translator);
		final BitSet workingSet = new BitSet();
		workingSet.set(0);
		workingSet.set(1);
		workingSet.set(7);
		workingSet.set(5);
		Shrinking.shrink(solver, workingSet, map);
	}

	@Test
	public void testExtensionLightDemand() {
		final Script script = setupScript(Logics.ALL);
		final DPLLEngine engine = new DPLLEngine(new DefaultLogger(), null);
		final Translator translator = new Translator();
		setupUnsatSet1(script, translator, engine);

		final ConstraintAdministrationSolver solver = new ConstraintAdministrationSolver(script, translator);
		solver.assertUnknownConstraint(1);
		final BitSet extension = solver.getSatExtension();

		System.out.println(extension.toString());
		Assert.assertTrue(extension.get(0));
	}

	@Test
	public void testExtensionMediumDemand() {
		final Script script = setupScript(Logics.ALL);
		final DPLLEngine engine = new DPLLEngine(new DefaultLogger(), null);
		final Translator translator = new Translator();
		setupUnsatSet1(script, translator, engine);

		final ConstraintAdministrationSolver solver = new ConstraintAdministrationSolver(script, translator);
		final BitSet extension = solver.getSatExtensionMoreDemanding();

		System.out.println(extension.toString());
		Assert.assertTrue(extension.get(0));
		Assert.assertTrue(extension.get(1));
		Assert.assertTrue(extension.get(2));
		Assert.assertFalse(extension.get(3));
		Assert.assertFalse(extension.get(4));
	}

	@Test
	public void testExtensionHeavyDemand() {
		final Script script = setupScript(Logics.ALL);
		final DPLLEngine engine = new DPLLEngine(new DefaultLogger(), null);
		final Translator translator = new Translator();
		setupUnsatSet1(script, translator, engine);

		final ConstraintAdministrationSolver solver = new ConstraintAdministrationSolver(script, translator);
		final BitSet extension = solver.getSatExtensionMaximalDemanding();

		System.out.println(extension.toString());
		Assert.assertTrue(extension.get(0));
		Assert.assertTrue(extension.get(1));
		Assert.assertTrue(extension.get(2));
		Assert.assertFalse(extension.get(3));
		Assert.assertTrue(extension.get(4));
	}

	@Test
	public void testMapBlockDown() {
		final Script script = setupScript(Logics.ALL);
		final DPLLEngine engine = new DPLLEngine(new DefaultLogger(), new SimpleTerminationRequest());
		final Translator translator = new Translator();
		setupUnsatSet3(script, translator, engine);

		final UnexploredMap map = new UnexploredMap(engine, translator);
		final BitSet set1 = new BitSet(3);
		final BitSet set2 = new BitSet(3);
		final BitSet set3 = new BitSet(3);
		final BitSet workingSet = new BitSet(3);
		workingSet.set(0, 3);
		set1.set(0);
		set1.set(1);
		set2.set(0);
		set2.set(2);
		set3.set(1);
		set3.set(2);
		map.BlockDown(set1);
		map.BlockDown(set2);
		map.BlockDown(set3);
		final BitSet unexplored = map.findMaximalUnexploredSubsetOf(workingSet);
		final BitSet crits = map.findImpliedCritsOf(workingSet);

		Assert.assertTrue(unexplored.cardinality() == 3);
		Assert.assertTrue(crits.cardinality() == 3);
	}

	@Test
	public void testMapBlockUp() {
		final Script script = setupScript(Logics.ALL);
		final DPLLEngine engine = new DPLLEngine(new DefaultLogger(), new SimpleTerminationRequest());
		;
		final Translator translator = new Translator();
		setupUnsatSet3(script, translator, engine);

		final UnexploredMap map = new UnexploredMap(engine, translator);
		final BitSet set1 = new BitSet(3);
		final BitSet set2 = new BitSet(3);
		final BitSet set3 = new BitSet(3);
		final BitSet workingSet = new BitSet(3);
		workingSet.set(0, 3);
		set1.set(0);
		set1.set(1);
		set2.set(0);
		set2.set(2);
		set3.set(1);
		set3.set(2);
		map.BlockUp(set1);
		map.BlockUp(set2);
		map.BlockUp(set3);
		final BitSet crits = map.findImpliedCritsOf(workingSet);
		final BitSet unexplored = map.findMaximalUnexploredSubsetOf(workingSet);

		Assert.assertTrue(unexplored.cardinality() == 1);
		Assert.assertTrue(crits.cardinality() == 0);
	}

	@Test
	public void testMapWorkingSet() {
		final Script script = setupScript(Logics.ALL);
		final DPLLEngine engine = new DPLLEngine(new DefaultLogger(), new SimpleTerminationRequest());
		final Translator translator = new Translator();
		setupUnsatSet3(script, translator, engine);

		final UnexploredMap map = new UnexploredMap(engine, translator);
		final BitSet workingSet = new BitSet(3);
		workingSet.set(1);
		workingSet.set(2);
		final BitSet crits = map.findImpliedCritsOf(workingSet);
		final BitSet unexplored = map.findMaximalUnexploredSubsetOf(workingSet);

		Assert.assertTrue(unexplored.get(1) == true);
		Assert.assertTrue(unexplored.get(2) == true);
		Assert.assertTrue(crits.cardinality() == 0);
	}

	@Test
	public void testMapNoUnexploredSet() {
		final Script script = setupScript(Logics.ALL);
		final DPLLEngine engine = new DPLLEngine(new DefaultLogger(), new SimpleTerminationRequest());
		final Translator translator = new Translator();
		setupUnsatSet3(script, translator, engine);

		final UnexploredMap map = new UnexploredMap(engine, translator);
		final BitSet set1 = new BitSet(3);
		final BitSet workingSet = new BitSet(3);
		workingSet.set(0);
		workingSet.set(2);
		set1.set(0);
		set1.set(2);
		map.BlockDown(set1);
		final BitSet unexplored = map.findMaximalUnexploredSubsetOf(workingSet);
		final BitSet crits = map.findImpliedCritsOf(workingSet);

		Assert.assertTrue(unexplored.cardinality() == 0);
		Assert.assertTrue(crits.cardinality() == 0);
	}

	@Test
	public void testMapExplicitlyForFindCrits() {
		final Script script = setupScript(Logics.ALL);
		final DPLLEngine engine = new DPLLEngine(new DefaultLogger(), new SimpleTerminationRequest());
		final Translator translator = new Translator();
		setupUnsatSet3(script, translator, engine);

		final UnexploredMap map = new UnexploredMap(engine, translator);
		final BitSet set1 = new BitSet(3);
		final BitSet set2 = new BitSet(3);
		final BitSet workingSet = new BitSet(3);
		set1.set(0);
		set1.set(1);
		set2.set(0);
		set2.set(2);
		workingSet.set(0);
		workingSet.set(1);
		map.BlockUp(set1);
		map.BlockDown(set2);
		final BitSet crits = map.findImpliedCritsOf(workingSet);
		final BitSet unexplored = map.findMaximalUnexploredSubsetOf(workingSet);

		Assert.assertFalse(crits.get(0));
		Assert.assertTrue(crits.get(1));
		Assert.assertFalse(crits.get(2));
		Assert.assertTrue(unexplored.cardinality() == 1);
	}

	@Test
	public void testReMusSet1() {
		final TerminationRequest request = new SimpleTerminationRequest();
		final Script script = setupScript(Logics.ALL, request);
		final DPLLEngine engine = new DPLLEngine(new DefaultLogger(), new SimpleTerminationRequest());
		final Translator translator = new Translator();
		setupUnsatSet1(script, translator, engine);

		final UnexploredMap map = new UnexploredMap(engine, translator);
		final ConstraintAdministrationSolver solver = new ConstraintAdministrationSolver(script, translator);
		final BitSet workingSet = new BitSet(5);
		workingSet.flip(0, 5);
		final IteratorReMus remus = new IteratorReMus(solver, map, workingSet);
		final ArrayList<MusContainer> muses = remus.enumerate();

		for (final MusContainer container : muses) {
			checkWhetherSetIsMus(container.getMus(), solver);
		}
		Assert.assertTrue(muses.size() == 1);
	}

	@Test
	public void testReMusSet2() {
		final Script script = setupScript(Logics.ALL);
		final DPLLEngine engine = new DPLLEngine(new DefaultLogger(), new SimpleTerminationRequest());
		final Translator translator = new Translator();
		setupUnsatSet2(script, translator, engine);

		final UnexploredMap map = new UnexploredMap(engine, translator);
		final ConstraintAdministrationSolver solver = new ConstraintAdministrationSolver(script, translator);
		final BitSet workingSet = new BitSet(10);
		workingSet.flip(0, 10);
		final IteratorReMus remus = new IteratorReMus(solver, map, workingSet);
		final ArrayList<MusContainer> muses = remus.enumerate();

		for (final MusContainer container : muses) {
			checkWhetherSetIsMus(container.getMus(), solver);
		}
		Assert.assertTrue(muses.size() == 15);
	}

	@Test
	public void testReMusSet5() {
		final Script script = setupScript(Logics.ALL);
		final DPLLEngine engine = new DPLLEngine(new DefaultLogger(), new SimpleTerminationRequest());
		final Translator translator = new Translator();
		setupUnsatSet5(script, translator, engine);

		final UnexploredMap map = new UnexploredMap(engine, translator);
		final ConstraintAdministrationSolver solver = new ConstraintAdministrationSolver(script, translator);
		final BitSet workingSet = new BitSet(10);
		workingSet.flip(0, 10);
		final IteratorReMus remus = new IteratorReMus(solver, map, workingSet);
		final ArrayList<MusContainer> muses = remus.enumerate();

		for (final MusContainer container : muses) {
			checkWhetherSetIsMus(container.getMus(), solver);
		}
		Assert.assertTrue(muses.size() == 15);
	}

	@Test
	public void testReMusEmptySet() {
		final Script script = setupScript(Logics.ALL);
		final DPLLEngine engine = new DPLLEngine(new DefaultLogger(), new SimpleTerminationRequest());
		final Translator translator = new Translator();
		setupUnsatSet2(script, translator, engine);

		final UnexploredMap map = new UnexploredMap(engine, translator);
		final ConstraintAdministrationSolver solver = new ConstraintAdministrationSolver(script, translator);
		final BitSet workingSet = new BitSet(10);
		final ReMus remus = new ReMus(solver, map, workingSet);
		final ArrayList<MusContainer> muses = remus.enumerate();

		Assert.assertTrue(muses.size() == 0);
	}

	@Test
	public void testReMusSet2WithTimeout() {
		final Script script = setupScript(Logics.ALL);
		final DPLLEngine engine = new DPLLEngine(new DefaultLogger(), new SimpleTerminationRequest());
		final Translator translator = new Translator();
		setupUnsatSet2(script, translator, engine);

		final UnexploredMap map = new UnexploredMap(engine, translator);
		final ConstraintAdministrationSolver solver = new ConstraintAdministrationSolver(script, translator);
		final BitSet workingSet = new BitSet(10);
		workingSet.flip(0, 10);
		final ReMus remus = new ReMus(solver, map, workingSet, 0);
		final ArrayList<MusContainer> muses = remus.enumerate();

		Assert.assertTrue(muses.size() == 0);
	}

	@Test(expected = SMTLIBException.class)
	public void testReMusWorkingSetTooBig() {
		final Script script = setupScript(Logics.ALL);
		final DPLLEngine engine = new DPLLEngine(new DefaultLogger(), new SimpleTerminationRequest());
		final Translator translator = new Translator();
		setupUnsatSet2(script, translator, engine);

		final UnexploredMap map = new UnexploredMap(engine, translator);
		final ConstraintAdministrationSolver solver = new ConstraintAdministrationSolver(script, translator);
		final BitSet workingSet = new BitSet(11);
		workingSet.set(11);
		final IteratorReMus remus = new IteratorReMus(solver, map, workingSet);
		remus.enumerate();
	}

	@Test
	public void testHeuristicSmallest() {
		final Script script = setupScript(Logics.ALL);
		final DPLLEngine engine = new DPLLEngine(new DefaultLogger(), new SimpleTerminationRequest());
		final Translator translator = new Translator();
		setupUnsatSet2(script, translator, engine);

		final UnexploredMap map = new UnexploredMap(engine, translator);
		final ConstraintAdministrationSolver solver = new ConstraintAdministrationSolver(script, translator);
		final BitSet workingSet = new BitSet(10);
		workingSet.flip(0, 10);
		final ReMus remus = new ReMus(solver, map, workingSet);
		final ArrayList<MusContainer> muses = remus.enumerate();

		final Random rnd = new Random(1337);
		Assert.assertTrue(Heuristics.chooseSmallestMus(muses, rnd).getMus().cardinality() == 2);
	}

	@Test
	public void testHeuristicBiggest() {
		final Script script = setupScript(Logics.ALL);
		final DPLLEngine engine = new DPLLEngine(new DefaultLogger(), new SimpleTerminationRequest());
		final Translator translator = new Translator();
		setupUnsatSet2(script, translator, engine);

		final UnexploredMap map = new UnexploredMap(engine, translator);
		final ConstraintAdministrationSolver solver = new ConstraintAdministrationSolver(script, translator);
		final BitSet workingSet = new BitSet(10);
		workingSet.flip(0, 10);
		final ReMus remus = new ReMus(solver, map, workingSet);
		final ArrayList<MusContainer> muses = remus.enumerate();

		final Random rnd = new Random(1337);
		Assert.assertTrue(Heuristics.chooseBiggestMus(muses, rnd).getMus().cardinality() == 3);
	}

	@Test
	public void testHeuristicLowestLexOrder() {
		final Script script = setupScript(Logics.ALL);
		final DPLLEngine engine = new DPLLEngine(new DefaultLogger(), new SimpleTerminationRequest());
		final Translator translator = new Translator();
		setupUnsatSet2(script, translator, engine);

		final UnexploredMap map = new UnexploredMap(engine, translator);
		final ConstraintAdministrationSolver solver = new ConstraintAdministrationSolver(script, translator);
		final BitSet workingSet = new BitSet(10);
		workingSet.flip(0, 10);
		final ReMus remus = new ReMus(solver, map, workingSet);
		final ArrayList<MusContainer> muses = remus.enumerate();

		final Random rnd = new Random(1337);
		final BitSet lowLexMus = Heuristics.chooseMusWithLowestLexicographicalOrder(muses, rnd).getMus();
		for (int i = lowLexMus.nextSetBit(0); i >= 0; i = lowLexMus.nextSetBit(i + 1)) {
			Assert.assertTrue(i == 0 || i == 2 || i == 5);
		}
	}

	@Test
	public void testHeuristicHighestLexOrder() {
		final Script script = setupScript(Logics.ALL);
		final DPLLEngine engine = new DPLLEngine(new DefaultLogger(), new SimpleTerminationRequest());
		final Translator translator = new Translator();
		setupUnsatSet2(script, translator, engine);

		final UnexploredMap map = new UnexploredMap(engine, translator);
		final ConstraintAdministrationSolver solver = new ConstraintAdministrationSolver(script, translator);
		final BitSet workingSet = new BitSet(10);
		workingSet.flip(0, 10);
		final ReMus remus = new ReMus(solver, map, workingSet);
		final ArrayList<MusContainer> muses = remus.enumerate();

		final Random rnd = new Random(1337);
		final BitSet highLexMus = Heuristics.chooseMusWithHighestLexicographicalOrder(muses, rnd).getMus();
		for (int i = highLexMus.nextSetBit(0); i >= 0; i = highLexMus.nextSetBit(i + 1)) {
			Assert.assertTrue(i == 8 || i == 9);
		}
	}

	@Test
	public void testHeuristicShallowest() {
		final Script script = setupScript(Logics.ALL);
		final DPLLEngine engine = new DPLLEngine(new DefaultLogger(), new SimpleTerminationRequest());
		final Translator translator = new Translator();
		setupUnsatSet2(script, translator, engine);

		final UnexploredMap map = new UnexploredMap(engine, translator);
		final ConstraintAdministrationSolver solver = new ConstraintAdministrationSolver(script, translator);
		final BitSet workingSet = new BitSet(10);
		workingSet.flip(0, 10);
		final ReMus remus = new ReMus(solver, map, workingSet);
		final ArrayList<MusContainer> muses = remus.enumerate();

		final Random rnd = new Random(1337);
		final BitSet shallowestMus = Heuristics.chooseShallowestMus(muses, rnd).getMus();
		Assert.assertTrue(shallowestMus.get(0));
	}

	@Test
	public void testHeuristicDeepest() {
		final Script script = setupScript(Logics.ALL);
		final DPLLEngine engine = new DPLLEngine(new DefaultLogger(), new SimpleTerminationRequest());
		final Translator translator = new Translator();
		setupUnsatSet2(script, translator, engine);

		final UnexploredMap map = new UnexploredMap(engine, translator);
		final ConstraintAdministrationSolver solver = new ConstraintAdministrationSolver(script, translator);
		final BitSet workingSet = new BitSet(10);
		workingSet.flip(0, 10);
		final ReMus remus = new ReMus(solver, map, workingSet);
		final ArrayList<MusContainer> muses = remus.enumerate();

		final Random rnd = new Random(1337);
		final BitSet deepestMus = Heuristics.chooseDeepestMus(muses, rnd).getMus();
		Assert.assertTrue(deepestMus.get(8));
	}

	@Test
	public void testHeuristicNarrowest() {
		final Script script = setupScript(Logics.ALL);
		final DPLLEngine engine = new DPLLEngine(new DefaultLogger(), new SimpleTerminationRequest());
		final Translator translator = new Translator();
		setupUnsatSet2(script, translator, engine);

		final UnexploredMap map = new UnexploredMap(engine, translator);
		final ConstraintAdministrationSolver solver = new ConstraintAdministrationSolver(script, translator);
		final BitSet workingSet = new BitSet(10);
		workingSet.flip(0, 10);
		final ReMus remus = new ReMus(solver, map, workingSet);
		final ArrayList<MusContainer> muses = remus.enumerate();

		final Random rnd = new Random(1337);
		final BitSet narrowestMus = Heuristics.chooseNarrowestMus(muses, rnd).getMus();
		final int width = narrowestMus.length() - narrowestMus.nextSetBit(0);
		Assert.assertTrue(width == 2);
	}

	@Test
	public void testHeuristicWidest() {
		final Script script = setupScript(Logics.ALL);
		final DPLLEngine engine = new DPLLEngine(new DefaultLogger(), new SimpleTerminationRequest());
		final Translator translator = new Translator();
		setupUnsatSet2(script, translator, engine);

		final UnexploredMap map = new UnexploredMap(engine, translator);
		final ConstraintAdministrationSolver solver = new ConstraintAdministrationSolver(script, translator);
		final BitSet workingSet = new BitSet(10);
		workingSet.flip(0, 10);
		final ReMus remus = new ReMus(solver, map, workingSet);
		final ArrayList<MusContainer> muses = remus.enumerate();

		final Random rnd = new Random(1337);
		final BitSet widestMus = Heuristics.chooseWidestMus(muses, rnd).getMus();
		final int width = widestMus.length() - widestMus.nextSetBit(0);
		Assert.assertTrue(width == 10);
	}

	@Test
	public void testHeuristicSmallestAmongWidest() {
		final Script script = setupScript(Logics.ALL);
		final DPLLEngine engine = new DPLLEngine(new DefaultLogger(), new SimpleTerminationRequest());
		final Translator translator = new Translator();
		// We use set 4 here, because in set 2 the widest mus is also one of the smallest.
		setupUnsatSet4(script, translator, engine);

		final UnexploredMap map = new UnexploredMap(engine, translator);
		final ConstraintAdministrationSolver solver = new ConstraintAdministrationSolver(script, translator);
		final BitSet workingSet = new BitSet(10);
		workingSet.flip(0, 10);
		final ReMus remus = new ReMus(solver, map, workingSet);
		final ArrayList<MusContainer> muses = remus.enumerate();

		final Random rnd = new Random(1337);
		final BitSet smallestAmongWidestMus = Heuristics.chooseSmallestAmongWidestMus(muses, 0.2, rnd).getMus();
		final int width = smallestAmongWidestMus.length() - smallestAmongWidestMus.nextSetBit(0);
		Assert.assertTrue(width == 8 || width == 9);
		Assert.assertTrue(smallestAmongWidestMus.cardinality() == 2);
	}

	@Test
	public void testHeuristicWidestAmongSmallest() {
		final Script script = setupScript(Logics.ALL);
		final DPLLEngine engine = new DPLLEngine(new DefaultLogger(), new SimpleTerminationRequest());
		final Translator translator = new Translator();
		// We use set 4 here, because in set 2 the widest mus is also one of the smallest.
		setupUnsatSet4(script, translator, engine);

		final UnexploredMap map = new UnexploredMap(engine, translator);
		final ConstraintAdministrationSolver solver = new ConstraintAdministrationSolver(script, translator);
		final BitSet workingSet = new BitSet(10);
		workingSet.flip(0, 10);
		final ReMus remus = new ReMus(solver, map, workingSet);
		final ArrayList<MusContainer> muses = remus.enumerate();

		final Random rnd = new Random(1337);
		final BitSet widestAmongSmallestMus = Heuristics.chooseWidestAmongSmallestMus(muses, 0.5, rnd).getMus();
		final int width = widestAmongSmallestMus.length() - widestAmongSmallestMus.nextSetBit(0);
		Assert.assertTrue(width == 10);
		Assert.assertTrue(widestAmongSmallestMus.cardinality() == 3);
	}

	@Test
	public void testNumberOfDifferentStatements() {
		final Script script = setupScript(Logics.ALL);
		final DPLLEngine engine = new DPLLEngine(new DefaultLogger(), new SimpleTerminationRequest());
		final Translator translator = new Translator();
		setupUnsatSet5(script, translator, engine);

		final UnexploredMap map = new UnexploredMap(engine, translator);
		final ConstraintAdministrationSolver solver = new ConstraintAdministrationSolver(script, translator);
		final BitSet workingSet = new BitSet(10);
		workingSet.flip(0, 10);
		final ReMus remus = new ReMus(solver, map, workingSet);
		final ArrayList<MusContainer> muses = remus.enumerate();

		int maxDifference = 0;
		int currentDifference = 0;
		for (final MusContainer container1 : muses) {
			for (final MusContainer container2 : muses) {
				currentDifference = Heuristics.numberOfDifferentStatements(container1, container2);
				if (currentDifference > maxDifference) {
					maxDifference = currentDifference;
				}
			}
		}
		Assert.assertTrue(maxDifference == 8);
	}

	@Test
	public void testHeuristicDifferentMusesWithRespectToStatements() {
		final Script script = setupScript(Logics.ALL);
		final DPLLEngine engine = new DPLLEngine(new DefaultLogger(), new SimpleTerminationRequest());
		final Translator translator = new Translator();
		setupUnsatSet5(script, translator, engine);

		final UnexploredMap map = new UnexploredMap(engine, translator);
		final ConstraintAdministrationSolver solver = new ConstraintAdministrationSolver(script, translator);
		final BitSet workingSet = new BitSet(10);
		workingSet.flip(0, 10);
		final ReMus remus = new ReMus(solver, map, workingSet);
		final ArrayList<MusContainer> muses = remus.enumerate();

		final Random rnd = new Random(1337);
		final ArrayList<MusContainer> differentMuses =
				Heuristics.chooseDifferentMusesWithRespectToStatements(muses, 13, rnd);
		int maxDifference = Integer.MIN_VALUE;
		int minDifference = Integer.MAX_VALUE;
		int currentDifference;
		for (final MusContainer container1 : differentMuses) {
			for (final MusContainer container2 : differentMuses) {
				if (container1.equals(container2)) {
					continue;
				}
				currentDifference = Heuristics.numberOfDifferentStatements(container1, container2);
				if (maxDifference < currentDifference) {
					maxDifference = currentDifference;
				}
				if (minDifference > currentDifference) {
					minDifference = currentDifference;
				}
			}
		}
		Assert.assertTrue(maxDifference == 8);
		Assert.assertTrue(minDifference == 2);
	}
}
