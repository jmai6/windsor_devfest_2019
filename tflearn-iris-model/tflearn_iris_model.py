import tflearn
import tensorflow as tf

from tflearn.data_utils import load_csv

data, labels = load_csv('iris_training.csv', target_column=-1,
                        categorical_labels=True, n_classes=3, has_header=True)

test_data, test_labels = load_csv('iris_test.csv', target_column=-1,
                                  categorical_labels=True, n_classes=3, has_header=True)

# build neural network
net = tflearn.input_data(shape=[None, 4], name='my_input')
net = tflearn.fully_connected(net, 32)
net = tflearn.fully_connected(net, 32)
net = tflearn.fully_connected(net, 3, activation='softmax', name='my_output')
net = tflearn.regression(net, optimizer='adam', learning_rate=0.001,
                         loss='categorical_crossentropy', name='target')

# deep neural network
model = tflearn.DNN(net, tensorboard_dir='tensorboard_logs/')

# Start training (apply gradient descent algorithm)
# model.fit(data, labels, n_epoch=100, batch_size=16, validation_set=(test_data, test_labels), show_metric=True)

# without test data running faster and more accurate. interesting!!!
model.fit(data, labels, n_epoch=1000, batch_size=16, show_metric=True)

predict_x = {
    'SepalLength': [5.1, 5.9, 6.9],
    'SepalWidth': [3.3, 3.0, 3.1],
    'PetalLength': [1.7, 4.2, 5.4],
    'PetalWidth': [0.5, 1.5, 2.1],
}

test1 = [5.1, 3.3, 1.7, 0.5]
test2 = [5.9, 3.0, 4.2, 1.5]
test3 = [6.9, 3.1, 5.4, 2.1]

pred = model.predict([test1, test2, test3])

print(pred)

#
# [[9.9599564e-01 4.0044012e-03 3.4370704e-23]
#  [1.3262039e-04 9.9966514e-01 2.0227558e-04]
#  [5.5267051e-11 4.3208950e-04 9.9956793e-01]]
# test1 => 0 ("Setosa"), test2 => 1 ("Versicolor"), test3 => 2 ("Virginica")
# from premade_estimator.py
# Prediction is "Setosa" (99.9%), expected "Setosa"
# Prediction is "Versicolor" (99.8%), expected "Versicolor"
# Prediction is "Virginica" (98.7%), expected "Virginica"

# Remove train ops
with net.graph.as_default():
    del tf.get_collection_ref(tf.GraphKeys.TRAIN_OPS)[:]

# Save the model
model.save('./output/iris.tflearn')
