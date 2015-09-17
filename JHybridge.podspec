Pod::Spec.new do |s|
  s.name         = "JHybridge"
  s.version      = "1.3.3"
  s.summary      = "Hybridge"

  s.description  = <<-DESC
                   When developing hybrid apps surely you'll need to access different native features and resources. 
                   Out there are plenty of bridge solutions. Hybridge tries to make easy communication and data 
                   exchanging between native (iOS & Android) and Javascript worlds, avoiding too much overhead.
                   DESC
  s.homepage     = "https://github.com/telefonicaid/j-hybridge"
  
  s.license      = { :type => "MIT", :file => "LICENSE.txt" }

  s.authors  = { 'Jsolutions' => 'leftmobile.com@gmail.com', 'David Garcia' => 'davidgarsan@gmail.com', 'Guillermo Gonzalez' => 'gonzalezreal@icloud.com' }

  s.platform     = :ios
  s.ios.deployment_target = "6.0"
  s.source       = { :git => "https://github.com/telefonicaid/j-hybridge.git", :tag => "1.3.3" }

  s.source_files = "ios/Hybridge/Hybridge/*.{h,m}"
  s.private_header_files = "ios/Hybridge/Hybridge/HYBURLProtocol.h", "ios/Hybridge/Hybridge/NSString+Hybridge.h"
  
  s.frameworks = "Foundation", "UIKit"
  s.requires_arc = true
end
