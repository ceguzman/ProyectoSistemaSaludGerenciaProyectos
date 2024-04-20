import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { ITypeDocument } from '../type-document.model';

@Component({
  standalone: true,
  selector: 'jhi-type-document-detail',
  templateUrl: './type-document-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class TypeDocumentDetailComponent {
  typeDocument = input<ITypeDocument | null>(null);

  previousState(): void {
    window.history.back();
  }
}
