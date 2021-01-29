# 0-1 Knapshak Problem

Given a set of items, each with a weight and a cost, determine the number of each item to include in a collection so
that the total weight is less than or equal to a given limit, and the total cost is as large as possible.

## Dependency
* Java11

## Usage

It can be used as a library like below

```java
import com.mobiquity.packer.Packer;
public class UsageExample {
    public void demo() {
        String output = Packer.pack("path/of/input_file");
        System.out.println(output);
    }
}
```

to use it as maven dependency

```xml
<dependency>
    <artifactId>implementation</artifactId>
    <groupId>com.mobiquity</groupId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```

**Build**
To build the jar
```bash
mvn clean install
```

**Tests**
To run the junits
```bash
mvn test
```

### Input file
**format** of input file will be like below

maxWeight : (index-1,weight-1,cost-1) (index-2,weight-2,cost-2) ... (index-n,weight-n,cost-n)
* value starts with €
* maxWeight, items' weight and items' cost can be decimal or integer

**Sample**
```text
81 : (1,53.38,€45) (2,88.62,€98) (3,78.48,€3) (4,72.30,€76) (5,30.18,€9) (6,46.34,€48)
8 : (1,15.3,€34)
75 : (1,85.31,€29) (2,14.55,€74) (3,3.98,€16) (4,26.24,€55) (5,63.69,€52) (6,76.25,€75) (7,60.02,€74) (8,93.18,€35) (9,89.95,€78)
56 : (1,90.72,€13) (2,33.80,€40) (3,43.15,€10) (4,37.97,€16) (5,46.81,€36) (6,48.77,€79) (7,81.80,€45) (8,19.36,€79) (9,6.76,€64)
```

### Output

items’ index numbers are separated by a comma or dash if no item selected
**Sample**
```text
4
-
2,7
8,9
```
Note: for some cases multiple output is possible. Like for last case 6,9 and 8,9 both are possible as cost for both item is same (€79) and both can fit in max weight. 
here lower weight have the priority.
