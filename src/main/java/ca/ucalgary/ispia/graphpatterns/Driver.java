package ca.ucalgary.ispia.graphpatterns;

import java.io.File;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

import ca.ucalgary.ispia.graphpatterns.tests.EvalTestRunner;
/**
 * The driver.
 * @author szrrizvi
 *
 */
public class Driver {
	/**
	 * The main control for specifying tasks.
	 * @param args
	 */
	public static void main(String[] args){	
		
		Driver d = new Driver();
		GraphDatabaseService graphDb = d.getGraphDb("simulation-tests/slashdot1riddb");
			
		EvalTestRunner etr = new EvalTestRunner(graphDb);
		//etr.warmup(250);
		//System.out.println("Done Warmup\n"); 
		etr.runGPHTestsList("simulation-tests/slashdottests/testCase", 6);
				
		
		
		/*for (int i = 1; i <= 6; i++){
			ObjectInputStream ois = null;
			List<GPHolder> tests = null;
			try {
				ois = new ObjectInputStream(new FileInputStream("performance-tests/testCase-" + i + ".ser"));
				tests = (List<GPHolder>) ois.readObject();
				ois.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return;
			}
			
			List<GPHolder> newTests = new ArrayList<GPHolder>();

			for (GPHolder test : tests){
				GraphPattern newGP = new GraphPattern();
				List<MyRelationship> newRels = new ArrayList<MyRelationship>();
				
				List<MyNode> nodes = test.getGp().getNodes();
				List<MyRelationship> rels = test.getGp().getAllRelationships();
				List<MyNode> res = test.getResultSchema();
				
				for(MyNode node : nodes){
					String id = null;
					if (node.hasAttribute("id")){
						id = node.getAttribute("id");
					}
					
					Map<String, String> attrs = new HashMap<String, String>();
					if (id != null){
						attrs.put("id", id);
					}
					node.setAttributes(attrs);
					newGP.addNode(node);
				}
				
				for (MyRelationship rel : rels){
					MyRelationship newRel = new MyRelationship(rel.getSource(), rel.getTarget(), RelType.RelA, rel.getId());
					newRels.add(newRel);
					newGP.addRelationship(newRel);
				}
				
				List<Pair<MyNode, MyNode>> mexList = new ArrayList<Pair<MyNode, MyNode>>();
				Map<String, MyNode> actMap = new HashMap<String, MyNode>();
				GPHolder newGPHolder = new GPHolder(newGP, mexList, actMap);
				newGPHolder.setResultSchema(test.getResultSchema());
				
				newTests.add(newGPHolder);
				
				System.out.println(test);
				System.out.println(newGPHolder);
				System.out.println("XXX");
			}
			
			try {
				ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("simulation-tests/slashdottests/testCase-"+i+".ser"));
				oos.writeObject(newTests);
				oos.close();
			} catch (Exception e){
				e.printStackTrace();
				return;
			}
		}*/
		
		
		/*Random rand = new Random(5523397);
		int[] sizes = {5, 7, 9, 10, 11, 13};
		int[] attrs = {1, 2, 4};
		int[] mex = {0, 1, 2};
		int[] resSizes = {1, 2, 4};
		int count = 1;
		for (int size : sizes){
			List<GPHolder> tests = new ArrayList<GPHolder>();
			for (int i = 0; i < 1000; i++){
				int vattrs = attrs[rand.nextInt(attrs.length)];
				int eattrs = attrs[rand.nextInt(attrs.length)];
				int mexs = mex[rand.nextInt(mex.length)];
				int resSize = resSizes[rand.nextInt(resSizes.length)];
				SubgraphGenerator sg = new SubgraphGenerator(graphDb, 82168, rand, size, 1.5d, 1, mexs, vattrs, eattrs, resSize);
				
				GPHolder gph = sg.createDBBasedGP();
				if (gph == null){
					i--;
				} else {
					tests.add(gph);
				}
			}
			
			try {
				ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("performance-tests/testCase-"+count+".ser"));
				count++;
				oos.writeObject(tests);
				oos.close();
			} catch (Exception e){
				e.printStackTrace();
				return;
			}
		}*/
		
		/*List<GPHolder> tests = null;
		 try {
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream("performance-tests/testCase-3.ser"));
			tests = (List<GPHolder>) ois.readObject();
			ois.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		
		 GPHolder test = tests.get(1);
		 EvalTestRunner etr = new EvalTestRunner(graphDb);
		 etr.executeSoloTestFC(test);
		 System.out.println("DONE FC\n");
		 etr.executeSoloTestFCCBJ(test);
		 System.out.println("DONE FC-CBJ\n");
		 etr.executeSoloTestFCLBJ(test);
		 System.out.println("DONE FC-LBJ\n");
		 
		 
		 
		for (GPHolder test : tests){
			List<MyRelationship> rels = test.getGp().getAllRelationships();
			for (MyRelationship rel : rels){
				System.out.println(rel.getSource().getId() + "->" + rel.getTarget().getId());
			}
			System.out.println();
		}*/
		
		
		graphDb.shutdown();
	} 

	/**
	 * Initialize the graph database
	 * @param db The name of directory containing the database
	 * @return The generated GraphDatabaseService object.
	 */
	private GraphDatabaseService getGraphDb(String db){
		File db_file = new File(db);

		GraphDatabaseService graphDb = new GraphDatabaseFactory().newEmbeddedDatabaseBuilder( db_file ).loadPropertiesFromFile("neo4j.properties").newGraphDatabase();
		registerShutdownHook( graphDb );

		return graphDb;
	}

	// START SNIPPET: shutdownHook
	private static void registerShutdownHook( final GraphDatabaseService graphDb )
	{
		// Registers a shutdown hook for the Neo4j instance so that it
		// shuts down nicely when the VM exits (even if you "Ctrl-C" the
		// running application).
		Runtime.getRuntime().addShutdownHook( new Thread()
		{
			@Override
			public void run()
			{
				graphDb.shutdown();
			}
		} );
	}
}
