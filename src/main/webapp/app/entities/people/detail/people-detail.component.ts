import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IPeople } from '../people.model';

@Component({
  standalone: true,
  selector: 'jhi-people-detail',
  templateUrl: './people-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class PeopleDetailComponent {
  people = input<IPeople | null>(null);

  previousState(): void {
    window.history.back();
  }
}
