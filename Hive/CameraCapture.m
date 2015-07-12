//
//  CameraCapture.m
//  Hive
//
//  Created by Denis Kubasov on 14/06/15.
//  Copyright (c) 2015 Denis Kubasov. All rights reserved.
//

#import <Foundation/Foundation.h>
#include "CameraCapture.h"

@implementation CameraCapture

@synthesize session;

- (id)init {
   self = [super init];
   if (self!=nil) {
      session = [[AVCaptureSession alloc] init];
      
      [session startRunning];
      
      // Add inputs and outputs.
      session.sessionPreset = AVCaptureSessionPresetLow;

      _previewLayer = [AVCaptureVideoPreviewLayer layerWithSession:session];
      
      NSNotificationCenter *center = [NSNotificationCenter defaultCenter];
      [center addObserver: self
                 selector: @selector(cameraListChanged:)
                     name: AVCaptureDeviceWasConnectedNotification
                   object: nil];
      [center addObserver: self
                 selector: @selector(cameraListChanged:)
                     name: AVCaptureDeviceWasDisconnectedNotification
                   object: nil];
   }
   return self;
}

- (void) setBounds: (NSRect) rect {
   _bounds = rect;
   _connection.videoPreviewLayer.frame = rect;
   [_connection.videoPreviewLayer setNeedsDisplay];
}

- (CALayer *) getPreviewLayer; {
   return _previewLayer;
}

- (NSString*) current {
   return _selectedCamera;
}

- (void) cameraListChanged:(NSNotification *)notification {
   [[NSNotificationCenter defaultCenter] postNotificationName:@"CameraListUpdated" object:self];
}

- (void) setMirror: (bool) enable {
   _connection.videoMirrored = enable;
   if(_subscriber) {
      [_subscriber mirroringChanged:enable];
   }}

- (bool) isMirrored {
   return _connection.videoMirrored;
}

- (bool) isOutputEnabled {
   return _outputEnabled;
}

- (void)selectCamera:(NSString*)name {
   
   _selectedCamera = name;
   
   [session beginConfiguration];
   [session removeConnection:_connection];
   
   for (AVCaptureDeviceInput *input in session.inputs) {
      [session removeInput:input];
   }
   
   NSArray *devices = [AVCaptureDevice devicesWithMediaType:AVMediaTypeVideo];
   for (AVCaptureDevice *device in devices) {
      
      if([name isEqualToString:[device localizedName]])
      {
         printf("Selected: %s\n", [name UTF8String]);
         NSError *error;
         AVCaptureDeviceInput *input =
         [AVCaptureDeviceInput deviceInputWithDevice:device error:&error];
         
         AVCaptureConnection *conn = [[AVCaptureConnection alloc ] initWithInputPort:input.ports[0] videoPreviewLayer:_previewLayer];
         conn.automaticallyAdjustsVideoMirroring = NO;
         conn.videoMirrored = YES;
         _connection = conn;
         [session addInputWithNoConnections:input];
         [session addConnection:conn];
         
         if(_subscriber) {
            [_subscriber inputChanged:input];
            [_subscriber mirroringChanged:_connection.videoMirrored];
         }
         break;
      }
   }
   
   [self setBounds:_bounds];
   [session commitConfiguration];
   [self enableOutput:_outputEnabled];
}

- (NSArray*) getCameraList {
   
   NSMutableArray *camList = [[NSMutableArray alloc] init];
   
   NSArray *devices = [AVCaptureDevice devicesWithMediaType:AVMediaTypeVideo];
   for (AVCaptureDevice *device in devices) {
      [camList addObject:[device localizedName]];
//      [_camera addItemWithObjectValue:[device localizedName]];
   }
   return camList;
}

- (void) subscribe: (NSObject <ProcessFrameDelegate>*) handler {
   _subscriber = handler;
   if(_subscriber) {
      [_subscriber mirroringChanged:[self isMirrored]];
   }
}

- (void)captureOutput:(AVCaptureOutput *)captureOutput
didOutputSampleBuffer:(CMSampleBufferRef)sampleBuffer
       fromConnection:(AVCaptureConnection *)connection {
   CVImageBufferRef image = CMSampleBufferGetImageBuffer(sampleBuffer);
   
   CFTypeID imageType = CFGetTypeID(image);
   
   if (imageType == CVPixelBufferGetTypeID()) {
      // Pixel Data
      CVPixelBufferRef pixel = image;
//      size_t w = CVPixelBufferGetWidth(pixel);
//      size_t h = CVPixelBufferGetHeight(pixel);
//      //printf("it's a pixel data! [%zu,%zu]\n", w, h);
//
//      CVPixelBufferLockBaseAddress(pixel, kCVPixelBufferLock_ReadOnly);
//      
//      printf("Base address plane0: %p\n", CVPixelBufferGetBaseAddressOfPlane(pixel, 0));
//      
//      CVPixelBufferUnlockBaseAddress(pixel, kCVPixelBufferLock_ReadOnly);

      if(_subscriber) {
         [_subscriber processFrame:pixel];
      }
      
//      NSMapTable *dict = [[NSMapTable alloc] init];
//      [dict setObject:(__bridge id)(pixel) forKey:@"PixelBuffer"] ;
//
//      [[NSNotificationCenter defaultCenter] postNotificationName:@"FrameToBeProcessed" object:self userInfo:[dict dictionaryRepresentation]];

   }
}

- (void) enableOutput: (bool) enable {

   _outputEnabled = enable;
   
   if(!_outputEnabled) {
      for (AVCaptureVideoDataOutput *output in [session outputs]) {
         [session removeOutput:output];
      }
      return;
   }
   
   AVCaptureVideoDataOutput *videoDataOutput = [AVCaptureVideoDataOutput new];
   //   NSDictionary *newSettings =
   //   @{ (NSString *)kCVPixelBufferPixelFormatTypeKey : @(kCVPixelFormatType_422YpCbCr8) };
   //   videoDataOutput.videoSettings = newSettings;
   
   //   videoDataOutput.videoSettings = nil;
   
   NSArray *formats = videoDataOutput.availableVideoCVPixelFormatTypes;
   for (NSNumber *format in formats) {
      printf("Supported format:, %lx == %x\n", [format longValue], kCVPixelFormatType_422YpCbCr8);
   }
   
   NSDictionary *newSettings =
   @{ (NSString *)kCVPixelBufferPixelFormatTypeKey : formats[0] };
   videoDataOutput.videoSettings = newSettings;
   
   // discard if the data output queue is blocked (as we process the still image
   [videoDataOutput setAlwaysDiscardsLateVideoFrames:NO];
   
   // create a serial dispatch queue used for the sample buffer delegate as well as when a still image is captured
   // a serial dispatch queue must be used to guarantee that video frames will be delivered in order
   // see the header doc for setSampleBufferDelegate:queue: for more information
   dispatch_queue_t videoDataOutputQueue = dispatch_queue_create("VideoDataOutputQueue", DISPATCH_QUEUE_SERIAL);
   [videoDataOutput setSampleBufferDelegate:self queue:videoDataOutputQueue];
   
   if ( [session canAddOutput:videoDataOutput] )
      [session addOutput:videoDataOutput];
}

@end
