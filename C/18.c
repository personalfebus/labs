#include "elem.h"
#include <stddef.h>
struct Elem *searchlist(struct Elem *list, int k)
{
        if (list == NULL) return NULL;
        else if ((list->tag == INTEGER) && (list->value.i == k))
            return list;
        else if (list->tag == LIST)
            if (searchlist(list->value.list, k) != NULL)
                return searchlist(list->value.list, k);
        return searchlist(list->tail, k);
}