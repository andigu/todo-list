#Style Notes

Just some code style notes I'm keeping record of to maintain consistency

## JavaScript
JavaScript conventions are the most murky I swear
* Use double quotes instead of single wherever a string is intended, but use single when it is intended
as a character (even though the outcome is the same). I.e. "Abc" vs. 'A'. Similar to Java
* Avoid doing assignments and checks that are different values. The trouble is, sometimes this may not work out, so I
 prefer to write a wrapper function. This msy not make sense, but, for example:
```ecmascript 6
location.hash = "hash";
if (location.hash == "hash") { // This should evaluate to true, but it doesn't
    
}
if (location.hash == "#hash") { // This shouldn't evaluate to true, but it does
    
}
// INSTEAD
location.hash = "hash";
function getHash() {
    return location.hash.substring(1);
}
if (getHash() == "hash") { // This will evaluate to true
    
}
```
* Use `===` comparator wherever possible just to avoid confusion
* Use `Object.hasOwnProperty` instead of `Object[property] !== undefined`

## Java
* Use builtin servlet `log` instead of `System.out.println`
* Instead of returning JSON's with parameters matching to null values, simply don't return a JSON with that parameter.
For example:
```json
{
"param": null,
"randomNumber": 1
}
```
should become
```json
{
"randomNumber": 1
}
```

