import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ITypeDocument } from '../type-document.model';
import { TypeDocumentService } from '../service/type-document.service';

@Component({
  standalone: true,
  templateUrl: './type-document-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class TypeDocumentDeleteDialogComponent {
  typeDocument?: ITypeDocument;

  protected typeDocumentService = inject(TypeDocumentService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.typeDocumentService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
