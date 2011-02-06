/**
 * @author cboesch
 */
function assert_equal(a,b){
    if (a==b) return true;
    else throw "expected:<"+a+"> but was:<"+b+">";
}

