class Particle {
	constructor(x, y, radius, dx, dy) {
		this.x = x;
		this.y = y;
		this.radius = radius;
		this.dx = dx;
		this.dy = dy;
		this.alpha = 1;
	}

	drawRelative(ctx, relativeX, relativeY, scale) {
		ctx.save();
		ctx.fillStyle = 'black';

		ctx.beginPath();
    // TODO: Remove SCREEN_MIDDLE_* variables defined in other files.
		ctx.arc((this.x - relativeX) * scale + SCREEN_MIDDLE_X, (this.y - relativeY) * scale + SCREEN_MIDDLE_Y, this.radius,
				0, Math.PI * 2, false);
		ctx.fill();
		ctx.restore();
	}

	update() {
		this.alpha -= 0.01;
		this.x += this.dx;
		this.y += this.dy;
	}
}

class ExplosionAnimation {
  constructor(centerX, centerY, numParticles) {
    this.particles = this.initParticles(centerX, centerY, numParticles);
  }

  initParticles(centerX, centerY, numParticles) {
    const particles = [];
    for (i = 0; i <= numParticles; i++) {
			let dx = (Math.random() - 0.5) * (Math.random() * 6);
			let dy = (Math.random() - 0.5) * (Math.random() * 6);
			let radius = Math.random() * 3;
			let particle = new Particle(centerX, centerY, radius, dx, dy);

			particles.push(particle);
		}
    return particles;
  }

  updateExplosion() {
    this.particles.forEach((particle, i) => {
			if (particle.alpha <= 0) {
				this.particles.splice(i, 1);
			} else {
        particle.update();
      }
		});
  }

  drawRelative(ctx, relativeX, relativeY, scale) {
    this.particles.forEach((particle, i) => {
      particle.drawRelative(ctx, relativeX, relativeY, scale);
		});
  }

  isAnimationOver() {
    return this.particles.length == 0;
  }
}
