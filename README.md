GenCor
======

##About

Using a text corpus' (Jane Austen novels) vocabulary and probability distribution model, will either generate or
correct sentences.

###Generating Text Using a Markov Model

Generates sentences using the trigram model. In this model, each  Xt is a discrete random variable 
denoting the  t-th word in a sentence, taking values from the vocabulary.

###Sentence Correction Using Hidden Markov Models

Using first-order Markov Models and the Viterbi algorithm to correct noisy sentences by giving the
most likely sequence computation.

##Usage

This software is a command line tool. The vocab and ngram files need to be located in the same directory
as the *.jar.

###Sentence Generation

Use the `-g` flag.<br />
`java -jar gencor.jar -g`

###Sentence Correction

Use the `-c` flag.<br />
`java -jar gencor.jar -c misspelled sentence to correct`

###Example Output

Sentence generation:
* `<s> I am very saucy . </s>`
* `<s> They could go directly to the comfort of the company of some great uncle . </s>`
* `<s> She was quite a different sort of young lady I learnt , in short , and all in want of complexion . </s>`
* `<s> She is loveliness itself . </s>`

Sentence correction:
* "she haf heard them"
  * `<s> She had heard them`
* "She was ulreedy quit live"
  * `<s> She was already quite like`
* "he said nit word by"
  * `<s> He said it would be`

##Format

The format for these files is as follows.  vocab.txt contains the vocabulary, one word per line:

1 word1<br />
2 word2<br />
...<br />
n wordn<br />

There are two special words in the vocabulary. The words  `<s>` and  `</s>` denote the beginning and 
the end of a sentence, respectively.

The files `*gram-counts.txt` contain a 3-gram probability model, with one conditional 
probability table entry per line. For `unigram-counts.txt`, each line of the file contains:

i  log10 P(xt = i)

For bigram-counts.txt, each line of the file contains:

i j log10 P(xt = j | xt-1 = i)

For trigram-counts.txt, each line of the file contains:

i  j  k log10 P(xt = k | xt-1 = j; xt-2 = i)

##To Do

* Dramatically improve performance
* Improve correction accuracy
* Improve documentation