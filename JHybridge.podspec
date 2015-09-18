Pod::Spec.new do |s|
  s.name         = "JHybridge"
  s.version      = "1.4.0"
  s.summary      = "Bridge for JS-objc."
  s.homepage     = "https://github.com/j14solutions/jhybridge"
  s.license      = 'MIT'
  s.author       = { "Jsolutions" => "leftmobile.com@gmail.com" }
  s.source       = { :git => "https://github.com/j14solutions/jhybridge.git", :tag => "1.4.0" }
  s.source_files = "ios/Hybridge/Hybridge/*.{h,m}"
  s.private_header_files = "ios/Hybridge/Hybridge/HYBURLProtocol.h", "ios/Hybridge/Hybridge/NSString+Hybridge.h"
  s.platform     = :ios, '6.1'
  s.ios.deployment_target = '6.1'
  s.requires_arc = true
end