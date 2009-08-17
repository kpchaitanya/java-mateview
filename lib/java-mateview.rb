
$CLASSPATH << File.expand_path(File.join(File.dirname(__FILE__), "..", "bin"))

require File.expand_path(File.join(File.dirname(__FILE__), "..", "lib", "jdom"))

class JavaMateView
  import com.redcareditor.mate.Grammar
  import com.redcareditor.mate.TextLocation
end

class Plist
  import com.redcareditor.plist.Dict
  import com.redcareditor.plist.PlistNode
  import com.redcareditor.plist.PlistPropertyLoader
end

class Onig
  import com.redcareditor.onig.Rx
  import com.redcareditor.onig.Match
end