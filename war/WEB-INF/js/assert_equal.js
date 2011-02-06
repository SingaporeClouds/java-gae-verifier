/**
 * @author cboesch
 */
function assert_equal(a,b){
    if (a==b) return true;
    else throw {exp: a, act: b};
}

