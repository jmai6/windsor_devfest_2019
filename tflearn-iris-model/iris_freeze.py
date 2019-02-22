import tensorflow as tf

with tf.Session() as session:
    my_saver = tf.train.import_meta_graph('./output/iris.tflearn.meta')
    my_saver.restore(session, tf.train.latest_checkpoint('./output/'))

    frozen_graph = tf.graph_util.convert_variables_to_constants(
        session,
        session.graph_def,
        ['my_output/Softmax'])

    for n in tf.get_default_graph().as_graph_def().node:
        print n.name

with open('./output/iris-frozen_model.pb', 'wb') as f:
    f.write(frozen_graph.SerializeToString())
