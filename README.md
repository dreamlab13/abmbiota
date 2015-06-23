# abmbiota
Agent based modeling of human gut microbiome interactions and perturbations.

# Agent based modeling of human gut microbiome interactions and perturbations

## Description 

This is an agent-based model of interactions between two bacterial specices and between species and the gut. 
The model is based on kinetic reactions describing bacterial fermentation of polysaccharides to acetate and 
propinate and fermentation of acetate to butyrate. The model confirms a hypothesis of feedbacks mechanisms 
necessity for providing functionality and stability of the system after disturbance. High fraction of bacterial community was shown to mutate during antibiotic treatment, though sensitive strains could become dominating after recovery. The recovery of sensitive strains is explained by fitness cost of the resistance. The model demonstrates not only quantitative dynamics of bacterial species, but al so gives an ability to observe the emergent spatial structure and its alteration, depending on various feedback mechanisms. Visual version of model shows that spatial structure is a key factor, 
which helps bacteria to survive and to adapt to changed environmental conditions. 


## Installation

The model was implemented in Java and can be run on any operationg system. To run the model you need Java 7.

You can download the model run script (IntractionsGutmodel.jar) from the last stable release 
from [this link](https://github.com/dreamlab13/abmbiota/tree/master/out/artifacts) and run it via coomand line.


## Running model

You can run the model from terminal with following command:

```java -jar InteractionsGutModel.jar - i <set.file> -o <output.file> [options]```

* -i ```<set.file>``` - input file with extendet program parameters
* -o```<output.file>``` - output file with numbers of components in the system at each time point

To view help for launch options and input parameters run ```java -jar InteractionsGutModel.jar --help```


## Example

You can download set file and run model via coomand line [test.ini](https://github.com/dreamlab13/abmbiota/tree/master/out/artifacts).
```
 java -jar InteractionsGutModel.jar -v -i test.ini -o test.output -mod 1
```    
After it has finished, a output file "test.output"  with count variables can be found in current directory.

## Full documentation

To see the full documentation visit [this link](https://github.com/dreamlab13/abmbiota/wiki).

## Contact

Tatiana Shashkova 
e-mail: tany-002@mail.ru


