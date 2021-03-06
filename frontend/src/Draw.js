// Draw sprites to be larger than their logical size, for smoother collisions.
const RENDER_SCALE = 1.2;

class Draw {
  constructor() {
    this.loadImages();
  }

  loadImages() {
    this.cloudImg = this.loadImage('cloud.png');
    this.parachuteImg = this.loadImage('parachute.png', 32, 32);
    const airplaneWhiteImg = this.loadImage('airplane_white.png', 32, 32);
    const airplaneBlackImg = this.loadImage('airplane_black.png', 32, 32);
    const bombPowerupImg = this.loadImage('bomb_powerup.png', 32, 32);
    const bombProjectileImg = this.loadImage('bomb_projectile.png', 32, 32);
    const medBoxImg = this.loadImage('med_box_powerup.png', 32, 32);
    const rocketPowerupImg = this.loadImage('rocket_powerup.png', 32, 32);
    const rocketProjectileImg = this.loadImage('rocket_projectile.png', 32, 32);
    const shrapnelPowerupImg = this.loadImage('shrapnel_powerup.png', 32, 32);
    const shrapnelProjectile = this.loadImage('shrapnel_projectile.png', 32, 32);
    const tripleshotImg = this.loadImage('tripleshot_powerup.png', 32, 32);
    const bulletImg = this.loadImage('bullet_projectile.png', 32, 32);

    this.airplaneImages = {
     'airplaneWhite': airplaneWhiteImg,
     'airplaneBlack': airplaneBlackImg
    };

    this.powerupImages = {
     'bomb': bombPowerupImg,
     'medkit': medBoxImg,
     'rocket': rocketPowerupImg,
     'shrapnel': shrapnelPowerupImg,
     'tripleshot': tripleshotImg
    };

    this.projectileImages = {
     'bullet': bulletImg,
     'bomb': bombProjectileImg,
     'rocket': rocketProjectileImg,
     'shrapnel': shrapnelProjectile,
     'tripleshot': bulletImg
    };

    this.currentWeaponImages = {
     'bombweapon': bombPowerupImg,
     'rocketweapon': rocketPowerupImg,
     'shrapnelweapon': shrapnelPowerupImg,
     'tripleshotweapon': tripleshotImg
    };
  }

  loadImage(imageSrc, width, height) {
    var img;
    if (width != null && height != null) {
      img = new Image(width, height);
    } else {
      img = new Image();
    }
    img.src = imageSrc;
    return img;
  }

  drawAirplane(ctx, airplaneColor, xCenter, yCenter, angle, width, height) {
    const image = this.airplaneImages[airplaneColor];
    const imgWidth = width == null ? image.width : width;
    const imgHeight = height == null ? image.height : height;
    ctx.setTransform(1, 0, 0, 1, xCenter, yCenter); // sets scale and origin
    ctx.rotate(-angle * Math.PI / 180);
    ctx.drawImage(image, -imgWidth / 2, -imgHeight / 2, imgWidth * RENDER_SCALE, imgHeight * RENDER_SCALE);
    ctx.setTransform(1, 0, 0, 1, 0, 0);
  }

  drawPowerUp(ctx, powerup, xCenter, yCenter, width, height) {
    if (!(powerup.name in this.powerupImages)) {
      return;
    }
    this.drawImage(ctx, this.parachuteImg, xCenter, yCenter - height, width, height);
    this.drawImage(ctx, this.powerupImages[powerup.name], xCenter, yCenter, width, height);
  }

  drawProjectile(ctx, projectile, x, y) {
    if (projectile.name == 'railgun') {
      ctx.beginPath();
      ctx.moveTo(x, y);
      ctx.lineTo(x + projectile.startX - projectile.xPos, y + projectile.startY - projectile.yPos);
      ctx.strokeStyle = '#1a53ff';
      ctx.lineWidth = projectile.radius;
      ctx.stroke();
      ctx.closePath();
    } else if (projectile.name in this.projectileImages) {
      ctx.setTransform(1, 0, 0, 1, x, y);
      ctx.rotate(-projectile.angle * Math.PI / 180);
      ctx.drawImage(this.projectileImages[projectile.name], -projectile.radius, -projectile.radius, projectile.radius * 2 * RENDER_SCALE, projectile.radius * 2 * RENDER_SCALE);
      ctx.setTransform(1, 0, 0, 1, 0, 0);
    } else {
      console.log('Error - invalid projectile name', projectile.name);
    }
  }

  drawCurrentUserWeapon(ctx, myPlayer) {
    if (myPlayer == null || myPlayer.weapon == null || !(myPlayer.weapon in this.currentWeaponImages)) {
      return;
    }
    var currentWeaponImg = this.currentWeaponImages[myPlayer.weapon];
    if (currentWeaponImg != null) {
      this.drawImage(ctx, currentWeaponImg, 50, 50, 64, 64);
    }
  }

  drawImage(ctx, image, xCenter, yCenter, width, height) {
    const imgWidth = width == null ? image.width : width;
    const imgHeight = height == null ? image.height : height;
    ctx.drawImage(image, xCenter - width / 2, yCenter - height / 2, imgWidth, imgHeight);
  }

  drawCircle(ctx, x, y, r, color='#b8e015') {
    ctx.beginPath();
    ctx.arc(x, y, r, 0, Math.PI*2);
    ctx.fillStyle = color;
    ctx.fill();
    ctx.closePath();
  }

  drawText(ctx, x, y, text, textAlign='left', color='black', font='30px Arial') {
    ctx.font = font;
    ctx.fillStyle = color;
    ctx.textAlign = textAlign;
    ctx.fillText(text, x, y);
  }

  drawHealthBar(ctx, x, y, health) {
    ctx.beginPath();
    ctx.rect(x - 75/2, y - 30, 75, 10);
    ctx.fillStyle = "#ff1100";
    ctx.fill();
    ctx.closePath();

    ctx.beginPath();
    ctx.rect(x - 75/2, y - 30, (health/4)*3, 10);
    ctx.fillStyle = "#11ff00";
    ctx.fill();
    ctx.closePath();
  }

  drawRect(ctx, x, y, width, height, color='#410765') {
    ctx.beginPath();
    ctx.rect(x,y,width,height);
    ctx.fillStyle = color;
    ctx.fill();
    ctx.closePath();
  }

  drawCloud(ctx, x, y) {
    ctx.drawImage(this.cloudImg, x, y);
  }
}

export default Draw;
