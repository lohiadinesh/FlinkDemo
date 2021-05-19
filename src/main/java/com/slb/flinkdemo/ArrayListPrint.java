package com.slb.flinkdemo;

import java.util.ArrayList;

import org.apache.beam.runners.flink.FlinkPipelineOptions;
import org.apache.beam.runners.flink.FlinkRunner;
import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.options.PipelineOptions;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.beam.sdk.transforms.Combine;
import org.apache.beam.sdk.transforms.Count;
import org.apache.beam.sdk.transforms.Create;
import org.apache.beam.sdk.transforms.MapElements;
import org.apache.beam.sdk.transforms.SimpleFunction;
import org.apache.beam.sdk.values.PCollection;

/**
 * @author Dinesh
 *
 */
public class ArrayListPrint {

	private static class MyMap extends SimpleFunction<Long, Long> {
		private static final long serialVersionUID = 1L;

		public Long apply(Long in) {
			System.out.println("Length is: " + in);
			return in;
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		PipelineOptions options = PipelineOptionsFactory.fromArgs(args).as(FlinkPipelineOptions.class);
		options.setRunner(FlinkRunner.class);

		// Direct Runner
		// PipelineOptions options =
		// PipelineOptionsFactory.fromArgs(args).withValidation().create();

		Pipeline p = Pipeline.create(options);

		// Create a PCollection from static objects
		ArrayList<String> strs = new ArrayList<String>();
		strs.add("Neil");
		strs.add("John");
		strs.add("Bob");

		PCollection<String> pc1 = p.apply(Create.of(strs));
		PCollection<Long> count = pc1.apply(Combine.globally(Count.<String>combineFn()).withoutDefaults());
		count.apply(MapElements.via(new MyMap()));

		System.out.println("About to run!");

		p.run().waitUntilFinish();

		System.out.println("Run complete!");

	}

}
// https://beam.apache.org/documentation/runners/flink/
