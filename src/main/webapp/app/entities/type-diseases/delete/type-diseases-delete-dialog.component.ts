import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ITypeDiseases } from '../type-diseases.model';
import { TypeDiseasesService } from '../service/type-diseases.service';

@Component({
  standalone: true,
  templateUrl: './type-diseases-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class TypeDiseasesDeleteDialogComponent {
  typeDiseases?: ITypeDiseases;

  protected typeDiseasesService = inject(TypeDiseasesService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.typeDiseasesService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
