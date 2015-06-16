//
//  ImageProcessor.h
//  Hive
//
//  Created by Denis Kubasov on 14/06/15.
//  Copyright (c) 2015 Denis Kubasov. All rights reserved.
//

#ifndef Hive_ImageProcessor_h
#define Hive_ImageProcessor_h

//@import AVFoundation;
#import <AVFoundation/AVFoundation.h>
#include "CameraCapture.h"

struct HarrisRef;

@interface ImageProcessor : NSObject <ProcessFrameDelegate>

- (id) init;
- (void) processFrame:(CVPixelBufferRef)pixelBuffer;
- (void) inputChanged: (AVCaptureDeviceInput*)input;
- (void) mirroringChanged: (bool) mirrored;

@property unsigned char* buffer;
@property CGDataProviderRef provider;
@property CGImageRef imageRef;
@property (strong) CALayer *drawLayer;
@property bool mirrored;

@property int width;
@property int height;

@property struct HarrisRef detector;

@end

#endif
