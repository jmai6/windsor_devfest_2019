import tensorflow as tf

input_arrays = ["my_input/X"]
output_arrays = ["my_output/Softmax"]
converter = tf.contrib.lite.TFLiteConverter.from_frozen_graph("./output/iris-frozen_model.pb", input_arrays, output_arrays)
tflite_model = converter.convert()
open("./output/iris.tflite", "wb").write(tflite_model)
