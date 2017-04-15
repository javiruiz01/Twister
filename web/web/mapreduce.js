String map =
"function() {" +
"  var text = this.text;" +
"  var id = this.id;" +
"  var words = text.match("/\w+ /g");" +
"  var tf = {};" +
"  for (var i = 0; i < words.length; i++) {" +
"    if (tf[words[i]] == null) {" +
"      tf[words[i]] = 1;" +
"    } else {" +
"      tf[words[i]] += 1;" +
"    }" +
"  }" +
"  for (w in tf) {" +
"    var ret = {};" +
"    ret[id] = tf[w];" +
"    emit(w, ret);" +
"  }" +
"}";

String reduce =
"function(k, v) {" +
"  var df = v.lenth;" +
"  var ret = [];" +
"  for (value in v) {" +
"    for (k in v[value]) {" +
"      ret[k] = v[value][k]*Math.log(N/df);" +
"    }" +
"  }" +
"  return ret;" +
"}";

String finalize =
"function(k, v) {" +
"  var df = v.length;" +
"  for (d in v) {" +
"    v[d] = v[d] * Math.log(N/df);" +
"  }" +
"  return v;" +
"}";
