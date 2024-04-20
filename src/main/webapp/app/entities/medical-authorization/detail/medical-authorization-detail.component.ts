import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IMedicalAuthorization } from '../medical-authorization.model';

@Component({
  standalone: true,
  selector: 'jhi-medical-authorization-detail',
  templateUrl: './medical-authorization-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class MedicalAuthorizationDetailComponent {
  medicalAuthorization = input<IMedicalAuthorization | null>(null);

  previousState(): void {
    window.history.back();
  }
}
