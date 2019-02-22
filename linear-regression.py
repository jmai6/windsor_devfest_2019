import tensorflow as tf
import numpy as np


test_data_size = 2000
iterations = 10000
learn_rate = 0.005


# create training data
def generate_training_data():
    train_x = []
    train_y = []

    for _ in xrange(test_data_size):
        x1 = np.random.rand()
        x2 = np.random.rand()
        x3 = np.random.rand()
        # y = w1*x1 + w2*x2 + w3*x3 + b
        y_f = 2 * x1 + 3 * x2 + 7 * x3 + 4
        train_x.append([x1, x2, x3])
        train_y.append(y_f)

    return np.array(train_x), np.transpose([train_y])


# define variables, initialize w1, w2, w3 and b to ZERO
x = tf.placeholder(tf.float32, [None, 3], name="x")
w = tf.Variable(tf.zeros([3, 1]), name="W")
b = tf.Variable(tf.zeros([1]), name="b")
y = tf.placeholder(tf.float32, [None, 1])

# define what to learning
learningResults = tf.add(tf.matmul(x, w), b)
# calculate cost (error)
cost = tf.reduce_mean(tf.square(y - learningResults))
# optimize w1, w2, w3 and b
train = tf.train.GradientDescentOptimizer(learn_rate).minimize(cost)
# generate data
train_input_data, train_expected_results = generate_training_data()

init = tf.global_variables_initializer()


with tf.Session() as session:
    session.run(init)

    for _ in xrange(iterations):
        session.run(train, feed_dict={
            x: train_input_data,
            y: train_expected_results
        })

    print "cost = {}".format(session.run(cost, feed_dict={
        x: train_input_data,
        y: train_expected_results
    }))

    print "W = {}".format(session.run(w))
    print "b = {}".format(session.run(b))

    # y_output = session.run([y], {x: [[1, 2, 3]], y: -1})
    # print (y_output)
