import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IClinicHistory } from '../clinic-history.model';

@Component({
  standalone: true,
  selector: 'jhi-clinic-history-detail',
  templateUrl: './clinic-history-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class ClinicHistoryDetailComponent {
  clinicHistory = input<IClinicHistory | null>(null);

  previousState(): void {
    window.history.back();
  }
}
