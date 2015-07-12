//
//  CameraCapture.h
//  Hive
//
//  Created by Denis Kubasov on 14/06/15.
//  Copyright (c) 2015 Denis Kubasov. All rights reserved.
//

#ifndef Hive_CameraCapture_h
#define Hive_CameraCapture_h

#import <AVFoundation/AVFoundation.h>

@protocol ProcessFrameDelegate <NSObject>

- (void) processFrame: (CVPixelBufferRef) pixelBuffer;
- (void) inputChanged: (AVCaptureDeviceInput*) input;
- (void) mirroringChanged: (bool) mirrored;

@end


@interface CameraCapture : NSObject <AVCaptureAudioDataOutputSampleBufferDelegate>

- (id)init;

- (NSArray*) getCameraList;
- (void)selectCamera:(NSString*)name;
- (NSString*) current;

- (CALayer *) getPreviewLayer;
- (void) setBounds: (NSRect) rect;
- (void) enableOutput: (bool) enable;
- (bool) isOutputEnabled;

- (void) setMirror: (bool) enable;
- (bool) isMirrored;

- (void) subscribe: (NSObject <ProcessFrameDelegate>*) handler;

- (void) cameraListChanged:(NSNotification *)notification;

@property (strong) AVCaptureSession *session;
@property (strong) AVCaptureVideoPreviewLayer *previewLayer;
@property (strong) AVCaptureConnection *connection;
@property bool outputEnabled;
@property NSString* selectedCamera;
@property (strong) NSObject<ProcessFrameDelegate> *subscriber;
@property (nonatomic) NSRect bounds;

- (void)captureOutput:(AVCaptureOutput *)captureOutput
didOutputSampleBuffer:(CMSampleBufferRef)sampleBuffer
fromConnection:(AVCaptureConnection *)connection;

@end

#endif
