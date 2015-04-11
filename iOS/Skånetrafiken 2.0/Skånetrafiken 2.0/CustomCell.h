//
//  CustomCell.h
//  Skånetrafiken 2.0
//
//  Created by Jacob Arvidsson on 2015-04-11.
//  Copyright (c) 2015 jacobarvidsson. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface CustomCell : UITableViewCell
@property (weak, nonatomic) IBOutlet UILabel *name;
@property (weak, nonatomic) IBOutlet UILabel *time;
@property (weak, nonatomic) IBOutlet UIView *status;
@property (weak, nonatomic) IBOutlet UIImageView *typeImage;
@property (weak, nonatomic) IBOutlet UILabel *destination;
@property (weak, nonatomic) IBOutlet UILabel *extraInfo;

@end
